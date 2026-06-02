import { Search, CalendarDays, Star, Home } from "lucide-react";
import { useAppContext } from "@/context/AppContext";
import { SearchBar } from "@/components/SearchBar";
import { PropertyCard } from "@/components/PropertyCard";
import { Tent, Palmtree, MountainSnow, Building2, Trees } from "lucide-react";
import { Link } from "wouter";
const CATEGORIES = [
  { id: "beach", label: "Beachfront", icon: Palmtree },
  { id: "cabin", label: "Cabins", icon: Tent },
  { id: "villa", label: "Villas", icon: Home },
  { id: "mountain", label: "Mountains", icon: MountainSnow },
  { id: "city", label: "City", icon: Building2 },
  { id: "countryside", label: "Countryside", icon: Trees }
];
function HomePage() {
  const { properties } = useAppContext();
  const featuredProperties = properties.slice(0, 8);
  return <div className="min-h-screen bg-background">
      {
    /* Hero Section */
  }
      <section className="relative pt-20 pb-32 px-4 flex flex-col items-center justify-center text-center overflow-hidden min-h-[80vh]">
        <div className="absolute inset-0 z-0">
          <img
    src="https://res.cloudinary.com/duaou63qp/image/upload/v1777878309/photo-1499793983690-e29da59ef1c2_ues570.jpg"
    alt="Hero background"
    className="w-full h-full object-cover brightness-75"
  />
          <div className="absolute inset-0 bg-gradient-to-b from-background/20 via-background/60 to-background" />
        </div>

        <div className="relative z-10 max-w-4xl mx-auto flex flex-col items-center gap-8">
          <h1 className="font-serif text-5xl md:text-7xl font-bold text-foreground tracking-tight leading-tight">
            Find your next <span className="text-primary italic">perfect</span> stay
          </h1>
          <p className="text-lg md:text-xl text-muted-foreground max-w-2xl">
            Discover unique homes, villas, and cabins for your next adventure.
            Experience the world like you belong there.
          </p>

          <div className="w-full mt-8">
            <SearchBar />
          </div>
        </div>
      </section>

      {
    /* Categories Row */
  }
      <section className="container mx-auto px-4 py-12 border-b">
        <div className="flex items-center justify-start overflow-x-auto no-scrollbar gap-8 pb-4">
          {CATEGORIES.map((cat) => {
    const Icon = cat.icon;
    return <Link
      key={cat.id}
      href={`/search?category=${cat.id}`}
      className="flex flex-col items-center gap-2 min-w-[80px] text-muted-foreground hover:text-foreground transition-colors group cursor-pointer"
      data-testid={`category-${cat.id}`}
    >
                <div className="p-3 rounded-full bg-muted/50 group-hover:bg-primary/10 group-hover:text-primary transition-colors">
                  <Icon className="w-6 h-6" />
                </div>
                <span className="text-sm font-medium">{cat.label}</span>
              </Link>;
  })}
        </div>
      </section>

      {
    /* Featured Properties */
  }
      <section className="container mx-auto px-4 py-16">
        <div className="flex items-end justify-between mb-8">
          <div>
            <h2 className="font-serif text-3xl font-bold text-foreground mb-2">Featured Homes</h2>
            <p className="text-muted-foreground">Handpicked properties for an unforgettable stay.</p>
          </div>
          <Link href="/search" className="text-sm font-semibold text-primary underline-offset-4 hover:underline" data-testid="link-view-all">
            View all
          </Link>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {featuredProperties.map((property) => <PropertyCard key={property.id} property={property} />)}
        </div>
      </section>

      {
    /* How it Works */
  }
      <section className="bg-secondary/50 py-20">
        <div className="container mx-auto px-4 text-center">
          <h2 className="font-serif text-3xl font-bold text-foreground mb-12">How Homistay Works</h2>
          <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
            {[
    { icon: Search, step: "1. Discover", desc: "Browse thousands of unique local homes curated for quality and comfort." },
    { icon: CalendarDays, step: "2. Book", desc: "Choose your dates and securely book your stay with instant confirmation." },
    { icon: Home, step: "3. Enjoy", desc: "Arrive, relax, and experience your destination like a true local." }
  ].map((item) => <div key={item.step} className="flex flex-col items-center gap-4">
                <div className="w-16 h-16 rounded-2xl bg-background flex items-center justify-center shadow-sm text-primary">
                  <item.icon className="w-8 h-8" />
                </div>
                <h3 className="text-xl font-bold">{item.step}</h3>
                <p className="text-muted-foreground">{item.desc}</p>
              </div>)}
          </div>
        </div>
      </section>

      {
    /* Testimonials */
  }
      <section className="container mx-auto px-4 py-20">
        <h2 className="font-serif text-3xl font-bold text-foreground mb-12 text-center">What our guests say</h2>
        <div className="grid md:grid-cols-3 gap-6">
          {[
    {
      name: "Sarah Jenkins",
      location: "Stayed in Florence",
      avatar: "https://res.cloudinary.com/duaou63qp/image/upload/v1777878375/photo-1438761681033-6461ffad8d80_lhsnbb.jpg",
      quote: "The villa was absolutely stunning. Every detail was perfect, and the host was incredibly welcoming. Best vacation ever."
    },
    {
      name: "David Chen",
      location: "Stayed in Tokyo",
      avatar: "https://res.cloudinary.com/duaou63qp/image/upload/v1777878397/photo-1507003211169-0a1dd7228f2d_jvmbgs.jpg",
      quote: "Finding a place this beautiful in the heart of Shibuya felt like a dream. The booking process was seamless."
    },
    {
      name: "Elena Rodriguez",
      location: "Stayed in Malibu",
      avatar: "https://res.cloudinary.com/duaou63qp/image/upload/v1777878408/photo-1544005313-94ddf0286df2_zakttf.jpg",
      quote: "Waking up to the sound of the ocean... I couldn't have asked for a better weekend getaway. Highly recommend Homistay."
    }
  ].map((test, i) => <div key={i} className="bg-card p-6 rounded-2xl shadow-sm border">
              <div className="flex items-center gap-1 text-primary mb-4">
                {[1, 2, 3, 4, 5].map((s) => <Star key={s} className="w-4 h-4 fill-current" />)}
              </div>
              <p className="text-foreground/80 italic mb-6">"{test.quote}"</p>
              <div className="flex items-center gap-3">
                <img src={test.avatar} alt={test.name} className="w-10 h-10 rounded-full object-cover" />
                <div>
                  <p className="font-bold text-sm">{test.name}</p>
                  <p className="text-xs text-muted-foreground">{test.location}</p>
                </div>
              </div>
            </div>)}
        </div>
      </section>

      {
    /* CTA Banner */
  }
      <section className="bg-primary py-16">
        <div className="container mx-auto px-4 text-center text-primary-foreground">
          <h2 className="font-serif text-3xl md:text-4xl font-bold mb-4">Ready to become a host?</h2>
          <p className="text-primary-foreground/80 mb-8 max-w-xl mx-auto">
            Turn your space into a source of income. Join thousands of hosts earning on Homistay.
          </p>
          <button onClick={() => {
            const token = localStorage.getItem("accessToken");
            if (token) {
              window.location.href = "/host/add-property";
            } else {
              window.dispatchEvent(new CustomEvent('open-auth', { detail: { mode: 'signup', asHost: true } }));
            }
          }} className="bg-white text-primary font-semibold px-8 py-3 rounded-full hover:bg-white/90 transition-colors cursor-pointer" data-testid="button-become-host">
            List your home
          </button>
        </div>
      </section>
    </div>;
}
export {
  HomePage
};
