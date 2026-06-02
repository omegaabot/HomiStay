import { useState, useMemo, useEffect } from "react";
import { useAppContext } from "@/context/AppContext";
import { PropertyCard } from "@/components/PropertyCard";
import { Slider } from "@/components/ui/slider";
import { Checkbox } from "@/components/ui/checkbox";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { SlidersHorizontal, X } from "lucide-react";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet";

const AMENITIES = ["WiFi", "Pool", "Kitchen", "Air conditioning", "Free parking", "Washer", "Dryer", "TV", "Gym", "Hot tub", "Indoor fireplace", "Workspace", "Ocean view", "Mountain view", "City view", "Garden view", "Beach access", "Patio or balcony", "Rooftop terrace", "Breakfast", "Elevator", "Kayak", "Sauna", "BBQ"];
const PROPERTY_TYPES = [
  { value: "villa", label: "Villa" },
  { value: "apartment", label: "Apartment" },
  { value: "cabin", label: "Cabin" },
  { value: "house", label: "House" },
  { value: "loft", label: "Loft" }
];

function useSearchParams() {
  const [search, setSearch] = useState(() => typeof window !== "undefined" ? window.location.search : "");
  useEffect(() => {
    const handleLocationChange = () => {
      setSearch(window.location.search);
    };
    window.addEventListener("locationchange", handleLocationChange);
    return () => window.removeEventListener("locationchange", handleLocationChange);
  }, []);
  const params = useMemo(() => new URLSearchParams(search), [search]);
  return {
    destination: params.get("destination") || "",
    checkin: params.get("checkin") || "",
    checkout: params.get("checkout") || "",
    guests: Number(params.get("guests") || "1"),
    category: params.get("category") || ""
  };
}

function SearchPage() {
  const { properties } = useAppContext();
  const { destination, category } = useSearchParams();
  const [priceRange, setPriceRange] = useState([0, 1000]);
  const [selectedTypes, setSelectedTypes] = useState([]);
  const [selectedAmenities, setSelectedAmenities] = useState([]);
  const [minRating, setMinRating] = useState("0");
  const [sortBy, setSortBy] = useState("featured");
  const [filtersOpen, setFiltersOpen] = useState(false);

  const filteredProperties = useMemo(() => {
    let results = properties.filter((p) => p.status === "active");
    if (destination) {
      const q = destination.toLowerCase();
      results = results.filter(
        (p) => p.location.city.toLowerCase().includes(q) || p.location.country.toLowerCase().includes(q) || p.title.toLowerCase().includes(q)
      );
    }
    if (category) {
      results = results.filter((p) => {
        const cat = category.toLowerCase();
        if (cat === "beach" || cat === "beachfront") {
          const city = p.location.city.toLowerCase();
          const country = p.location.country.toLowerCase();
          return city.includes("goa") || country.includes("malibu") || country.includes("maldives") || p.amenities.includes("Beach access") || p.amenities.includes("Ocean view");
        }
        if (cat === "mountain" || cat === "mountains") {
          const city = p.location.city.toLowerCase();
          const country = p.location.country.toLowerCase();
          return city.includes("manali") || country.includes("chamonix") || country.includes("norway") || p.type === "cabin" || p.amenities.includes("Mountain view");
        }
        if (cat === "city") {
          const city = p.location.city.toLowerCase();
          return city.includes("tokyo") || city.includes("mumbai") || city.includes("delhi") || city.includes("bengaluru") || city.includes("new york") || p.type === "apartment" || p.type === "loft";
        }
        if (cat === "countryside") {
          const country = p.location.country.toLowerCase();
          return country.includes("italy") || country.includes("scotland") || country.includes("morocco") || country.includes("norway");
        }
        return p.category === cat;
      });
    }
    results = results.filter((p) => p.price >= priceRange[0] && p.price <= priceRange[1]);
    if (selectedTypes.length > 0) {
      results = results.filter((p) => selectedTypes.includes(p.type));
    }
    if (selectedAmenities.length > 0) {
      results = results.filter((p) => selectedAmenities.every((a) => p.amenities.includes(a)));
    }
    if (minRating !== "0") {
      results = results.filter((p) => p.rating >= Number(minRating));
    }
    switch (sortBy) {
      case "price_asc":
        results.sort((a, b) => a.price - b.price);
        break;
      case "price_desc":
        results.sort((a, b) => b.price - a.price);
        break;
      case "rating":
        results.sort((a, b) => b.rating - a.rating);
        break;
      case "reviews":
        results.sort((a, b) => b.reviewCount - a.reviewCount);
        break;
    }
    return results;
  }, [properties, destination, category, priceRange, selectedTypes, selectedAmenities, minRating, sortBy]);

  const toggleType = (type) => {
    setSelectedTypes((prev) => prev.includes(type) ? prev.filter((t) => t !== type) : [...prev, type]);
  };
  const toggleAmenity = (amenity) => {
    setSelectedAmenities((prev) => prev.includes(amenity) ? prev.filter((a) => a !== amenity) : [...prev, amenity]);
  };
  const activeFilterCount = selectedTypes.length + selectedAmenities.length + (minRating !== "0" ? 1 : 0) + (priceRange[0] > 0 || priceRange[1] < 1000 ? 1 : 0);

  const FilterPanel = () => <div className="space-y-6">
      <div>
        <h3 className="font-semibold mb-3">Price per night</h3>
        <div className="px-1">
          <Slider
    min={0}
    max={1000}
    step={10}
    value={priceRange}
    onValueChange={setPriceRange}
    className="mb-3"
    data-testid="price-slider"
  />
          <div className="flex justify-between text-sm text-muted-foreground">
            <span>${priceRange[0]}</span>
            <span>${priceRange[1]}{priceRange[1] === 1000 ? "+" : ""}</span>
          </div>
        </div>
      </div>

      <div className="border-t pt-6">
        <h3 className="font-semibold mb-3">Property type</h3>
        <div className="space-y-2.5">
          {PROPERTY_TYPES.map((type) => <label key={type.value} className="flex items-center gap-3 cursor-pointer">
              <Checkbox
    checked={selectedTypes.includes(type.value)}
    onCheckedChange={() => toggleType(type.value)}
    data-testid={`filter-type-${type.value}`}
  />
              <span className="text-sm">{type.label}</span>
            </label>)}
        </div>
      </div>

      <div className="border-t pt-6">
        <h3 className="font-semibold mb-3">Minimum rating</h3>
        <Select value={minRating} onValueChange={setMinRating}>
          <SelectTrigger data-testid="filter-rating">
            <SelectValue placeholder="Any rating" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="0">Any rating</SelectItem>
            <SelectItem value="4">4.0+</SelectItem>
            <SelectItem value="4.5">4.5+</SelectItem>
            <SelectItem value="4.8">4.8+</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="border-t pt-6">
        <h3 className="font-semibold mb-3">Amenities</h3>
        <div className="space-y-2.5">
          {AMENITIES.map((amenity) => <label key={amenity} className="flex items-center gap-3 cursor-pointer">
              <Checkbox
    checked={selectedAmenities.includes(amenity)}
    onCheckedChange={() => toggleAmenity(amenity)}
    data-testid={`filter-amenity-${amenity}`}
  />
              <span className="text-sm">{amenity}</span>
            </label>)}
        </div>
      </div>

      <div className="border-t pt-6">
        <Button
    variant="outline"
    className="w-full"
    onClick={() => {
      setPriceRange([0, 1000]);
      setSelectedTypes([]);
      setSelectedAmenities([]);
      setMinRating("0");
    }}
    data-testid="button-clear-filters"
  >
          Clear all filters
        </Button>
      </div>
    </div>;

  return <div className="container mx-auto px-4 py-8 max-w-7xl">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <div>
          <h1 className="font-serif text-2xl font-bold">
            {destination ? `Stays in "${destination}"` : category ? `${category.charAt(0).toUpperCase() + category.slice(1)} properties` : "All properties"}
          </h1>
          <p className="text-muted-foreground text-sm mt-1">{filteredProperties.length} properties found</p>
        </div>
        <div className="flex items-center gap-3">
          <Sheet open={filtersOpen} onOpenChange={setFiltersOpen}>
            <SheetTrigger asChild>
              <Button variant="outline" className="flex items-center gap-2 rounded-full" data-testid="button-filters">
                <SlidersHorizontal className="w-4 h-4" />
                Filters
                {activeFilterCount > 0 && <Badge className="ml-1 h-5 w-5 p-0 flex items-center justify-center text-xs rounded-full">
                    {activeFilterCount}
                  </Badge>}
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-80 overflow-y-auto">
              <SheetHeader>
                <SheetTitle>Filters</SheetTitle>
              </SheetHeader>
              <div className="mt-6">
                <FilterPanel />
              </div>
            </SheetContent>
          </Sheet>

          <Select value={sortBy} onValueChange={setSortBy}>
            <SelectTrigger className="w-44 rounded-full" data-testid="select-sort">
              <SelectValue placeholder="Sort by" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="featured">Featured</SelectItem>
              <SelectItem value="price_asc">Price: Low to High</SelectItem>
              <SelectItem value="price_desc">Price: High to Low</SelectItem>
              <SelectItem value="rating">Highest Rated</SelectItem>
              <SelectItem value="reviews">Most Reviewed</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      {activeFilterCount > 0 && <div className="flex flex-wrap gap-2 mb-6">
          {selectedTypes.map((t) => <Badge key={t} variant="secondary" className="gap-1 px-3 py-1.5 rounded-full">
              {PROPERTY_TYPES.find((pt) => pt.value === t)?.label}
              <X className="w-3 h-3 cursor-pointer" onClick={() => toggleType(t)} />
            </Badge>)}
          {selectedAmenities.map((a) => <Badge key={a} variant="secondary" className="gap-1 px-3 py-1.5 rounded-full">
              {a}
              <X className="w-3 h-3 cursor-pointer" onClick={() => toggleAmenity(a)} />
            </Badge>)}
          {minRating !== "0" && <Badge variant="secondary" className="gap-1 px-3 py-1.5 rounded-full">
              Rating {minRating}+
              <X className="w-3 h-3 cursor-pointer" onClick={() => setMinRating("0")} />
            </Badge>}
        </div>}

      <div className="flex gap-8">
        <aside className="hidden lg:block w-64 shrink-0 self-start sticky top-24 max-h-[calc(100vh-8rem)] overflow-y-auto pr-2 pb-6">
          <FilterPanel />
        </aside>

        <div className="flex-1">
          {filteredProperties.length === 0 ? (
            <div className="text-center py-20 bg-card rounded-2xl border">
              <p className="text-2xl font-serif font-bold mb-2">No properties found</p>
              <p className="text-muted-foreground">Try adjusting your filters or search for a different destination.</p>
              <Button
                className="mt-6 rounded-xl"
                onClick={() => {
                  setPriceRange([0, 1000]);
                  setSelectedTypes([]);
                  setSelectedAmenities([]);
                  setMinRating("0");
                }}
              >
                Clear filters
              </Button>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6">
              {filteredProperties.map((property) => (
                <PropertyCard key={property.id} property={property} />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>;
}

export {
  SearchPage
};
