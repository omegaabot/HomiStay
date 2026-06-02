import { useState, useEffect } from "react";
import { useLocation } from "wouter";
import { Search, MapPin, CalendarDays, Users } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { Calendar } from "./ui/calendar";
import { format } from "date-fns";
import { cn } from "@/lib/utils";
import { useAppContext } from "@/context/AppContext";

// Intercept pushState and replaceState to dispatch a custom 'locationchange' event.
// This allows the SearchPage to update immediately when search query parameters change.
if (typeof window !== "undefined" && !window.__locationChangePatched) {
  window.__locationChangePatched = true;
  const originalPush = window.history.pushState;
  window.history.pushState = function (...args) {
    const result = originalPush.apply(this, args);
    window.dispatchEvent(new Event("locationchange"));
    return result;
  };
  const originalReplace = window.history.replaceState;
  window.history.replaceState = function (...args) {
    const result = originalReplace.apply(this, args);
    window.dispatchEvent(new Event("locationchange"));
    return result;
  };
  window.addEventListener("popstate", () => {
    window.dispatchEvent(new Event("locationchange"));
  });
}

function GuestRow({ label, desc, value, min, max, onChange }) {
  return (
    <div className="flex items-center justify-between">
      <div>
        <p className="font-medium text-sm">{label}</p>
        <p className="text-xs text-muted-foreground">{desc}</p>
      </div>
      <div className="flex items-center gap-3">
        <Button type="button" variant="outline" size="icon" className="w-8 h-8 rounded-full" disabled={value <= min} onClick={() => onChange(Math.max(min, value - 1))}>-</Button>
        <span className="w-5 text-center text-sm font-medium">{value}</span>
        <Button type="button" variant="outline" size="icon" className="w-8 h-8 rounded-full" disabled={value >= max} onClick={() => onChange(Math.min(max, value + 1))}>+</Button>
      </div>
    </div>
  );
}

function SearchBar({ variant = "hero", className = "" }) {
  const [, setLocation] = useLocation();
  const { properties } = useAppContext();

  // Extract unique cities and countries as potential destinations (active properties only)
  const activeProperties = properties.filter((p) => p.status === "active");
  const destinations = [
    ...new Set(
      [
        ...activeProperties.map((p) => p.location?.city),
        ...activeProperties.map((p) => p.location?.country),
      ].filter(Boolean)
    ),
  ];

  // Helper to read search params from URL
  const getSearchParams = () => {
    if (typeof window === "undefined") return { destination: "", checkin: "", checkout: "", adults: "2" };
    const params = new URLSearchParams(window.location.search);
    return {
      destination: params.get("destination") || "",
      checkin: params.get("checkin") || "",
      checkout: params.get("checkout") || "",
      adults: params.get("adults") || params.get("guests") || "2",
      children: params.get("children") || "0",
      infants: params.get("infants") || "0",
      pets: params.get("pets") || "0",
    };
  };

  const urlParams = getSearchParams();
  const [destination, setDestination] = useState(urlParams.destination);
  const [checkinDate, setCheckinDate] = useState(urlParams.checkin ? new Date(urlParams.checkin) : undefined);
  const [checkoutDate, setCheckoutDate] = useState(urlParams.checkout ? new Date(urlParams.checkout) : undefined);
  const [adults, setAdults] = useState(Number(urlParams.adults));
  const [children, setChildren] = useState(Number(urlParams.children));
  const [infants, setInfants] = useState(Number(urlParams.infants));
  const [pets, setPets] = useState(Number(urlParams.pets));
  const [isFocused, setIsFocused] = useState(false);

  // Keep inputs synchronized with back/forward navigation or URL updates
  useEffect(() => {
    const handleLocationChange = () => {
      const p = getSearchParams();
      setDestination(p.destination);
      setCheckinDate(p.checkin ? new Date(p.checkin) : undefined);
      setCheckoutDate(p.checkout ? new Date(p.checkout) : undefined);
      setGuests(p.guests);
    };
    window.addEventListener("locationchange", handleLocationChange);
    return () => window.removeEventListener("locationchange", handleLocationChange);
  }, []);

  // Filter destinations based on search query when user starts typing
  const suggestions = destination.trim()
    ? destinations.filter((dest) =>
        dest.toLowerCase().includes(destination.trim().toLowerCase())
      )
    : [];

  const handleSearch = (e) => {
    e.preventDefault();
    triggerSearch(destination);
  };

  const triggerSearch = (destVal) => {
    const params = new URLSearchParams();
    if (destVal) params.append("destination", destVal.trim());
    if (checkinDate) params.append("checkin", format(checkinDate, "yyyy-MM-dd"));
    if (checkoutDate) params.append("checkout", format(checkoutDate, "yyyy-MM-dd"));
    params.append("adults", String(adults));
    params.append("children", String(children));
    if (infants > 0) params.append("infants", String(infants));
    if (pets > 0) params.append("pets", String(pets));
    setLocation(`/search?${params.toString()}`);
  };

  const selectSuggestion = (destVal) => {
    setDestination(destVal);
    triggerSearch(destVal);
  };

  if (variant === "compact") {
    return (
      <form
        onSubmit={handleSearch}
        className={cn(
          "flex items-center w-full max-w-sm border rounded-full shadow-sm hover:shadow-md transition-shadow bg-background px-2 py-1 relative",
          className
        )}
      >
        <div className="flex-1 px-4 text-sm font-medium relative">
          <Input
            type="text"
            placeholder="Anywhere"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
            className="border-0 shadow-none focus-visible:ring-0 px-0 h-8 text-sm w-full"
            autoComplete="off"
          />
          
          {/* Dynamic Suggestion Dropdown */}
          {isFocused && suggestions.length > 0 && (
            <div className="absolute top-full left-0 right-0 mt-3 bg-background border border-border rounded-2xl shadow-xl z-[100] py-1 max-h-48 overflow-y-auto text-left" style={{ maxHeight: "12rem" }}>
              {suggestions.map((dest) => (
                <button
                  key={dest}
                  type="button"
                  onMouseDown={() => selectSuggestion(dest)}
                  className="w-full px-4 py-2 text-sm text-foreground hover:bg-muted transition-colors text-left flex items-center gap-2 cursor-pointer"
                >
                  <MapPin className="w-4 h-4 text-muted-foreground shrink-0" />
                  <span className="font-medium">{dest}</span>
                </button>
              ))}
            </div>
          )}
        </div>

        <Button type="submit" size="icon" className="rounded-full h-8 w-8 shrink-0">
          <Search className="w-4 h-4" />
        </Button>
      </form>
    );
  }

  return (
    <form
      onSubmit={handleSearch}
      className={cn(
        "flex flex-col md:flex-row items-center w-full max-w-4xl bg-background rounded-full p-2 shadow-xl border border-border/50 gap-0 relative z-[60]",
        className
      )}
      data-testid="hero-search-bar"
    >
      {/* Where */}
      <Popover>
        <PopoverTrigger asChild>
          <button
            type="button"
            className="flex-1 w-full md:w-auto px-6 py-3 rounded-full hover:bg-muted/50 transition-colors text-left"
            data-testid="search-segment-where"
          >
            <div className="text-xs font-bold text-foreground">Where</div>
            <div className="text-sm text-muted-foreground">{destination || "Search destinations"}</div>
          </button>
        </PopoverTrigger>
        <PopoverContent className="w-80 p-4" align="start">
          <div className="space-y-3">
            <Input
              type="text"
              placeholder="Search destinations"
              value={destination}
              onChange={(e) => setDestination(e.target.value)}
              autoFocus
              className="rounded-xl"
              autoComplete="off"
            />
            {suggestions.length > 0 && (
              <div className="max-h-60 overflow-y-auto space-y-1">
                {suggestions.map((dest) => (
                  <button
                    key={dest}
                    type="button"
                    onMouseDown={() => { setDestination(dest); }}
                    className="w-full px-3 py-2 text-sm hover:bg-muted rounded-lg text-left flex items-center gap-2"
                  >
                    <MapPin className="w-4 h-4 text-muted-foreground shrink-0" />
                    <span className="font-medium">{dest}</span>
                  </button>
                ))}
              </div>
            )}
          </div>
        </PopoverContent>
      </Popover>

      <div className="hidden md:block w-px h-10 bg-border shrink-0" />

      {/* Check in */}
      <Popover>
        <PopoverTrigger asChild>
          <button
            type="button"
            className="w-full md:w-auto px-6 py-3 rounded-full hover:bg-muted/50 transition-colors text-left"
            data-testid="search-segment-checkin"
          >
            <div className="text-xs font-bold text-foreground">Check in</div>
            <div className="text-sm text-muted-foreground">
              {checkinDate ? format(checkinDate, "MMM d") : "Add date"}
            </div>
          </button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="start">
          <Calendar
            mode="single"
            selected={checkinDate}
            onSelect={setCheckinDate}
            disabled={{ before: new Date() }}
            initialFocus
          />
        </PopoverContent>
      </Popover>

      <div className="hidden md:block w-px h-10 bg-border shrink-0" />

      {/* Check out */}
      <Popover>
        <PopoverTrigger asChild>
          <button
            type="button"
            className="w-full md:w-auto px-6 py-3 rounded-full hover:bg-muted/50 transition-colors text-left"
            data-testid="search-segment-checkout"
          >
            <div className="text-xs font-bold text-foreground">Check out</div>
            <div className="text-sm text-muted-foreground">
              {checkoutDate ? format(checkoutDate, "MMM d") : "Add date"}
            </div>
          </button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="start">
          <Calendar
            mode="single"
            selected={checkoutDate}
            onSelect={setCheckoutDate}
            disabled={{ before: checkinDate || new Date() }}
            initialFocus
          />
        </PopoverContent>
      </Popover>

      <div className="hidden md:block w-px h-10 bg-border shrink-0" />

      {/* Who */}
      <Popover>
        <PopoverTrigger asChild>
          <button
            type="button"
            className="w-full md:w-auto px-6 py-3 rounded-full hover:bg-muted/50 transition-colors text-left"
            data-testid="search-segment-guests"
          >
            <div className="text-xs font-bold text-foreground">Who</div>
            <div className="text-sm text-muted-foreground">
              {adults + children} guest{adults + children !== 1 ? "s" : ""}
              {infants > 0 ? `, ${infants} infant${infants > 1 ? "s" : ""}` : ""}
              {pets > 0 ? `, ${pets} pet${pets > 1 ? "s" : ""}` : ""}
            </div>
          </button>
        </PopoverTrigger>
        <PopoverContent className="w-72 p-4" align="start">
          <div className="space-y-4">
            <GuestRow label="Adults" desc="Ages 13+" value={adults} min={1} max={16} onChange={setAdults} />
            <GuestRow label="Children" desc="Ages 2-12" value={children} min={0} max={16} onChange={setChildren} />
            <GuestRow label="Infants" desc="Under 2" value={infants} min={0} max={5} onChange={setInfants} />
            <GuestRow label="Pets" desc="Service animals welcome" value={pets} min={0} max={5} onChange={setPets} />
          </div>
        </PopoverContent>
      </Popover>

      {/* Search */}
      <Button
        type="submit"
        size="icon"
        className="rounded-full h-12 w-12 shrink-0 bg-primary hover:bg-primary/90 ml-auto md:ml-2 mt-2 md:mt-0"
      >
        <Search className="w-5 h-5" />
      </Button>
    </form>
  );
}

export { SearchBar };
