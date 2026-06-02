import { useEffect, useState } from "react";
import { useLocation } from "wouter";
import { useAppContext } from "@/context/AppContext";
import { PropertyCard } from "@/components/PropertyCard";
import { Button } from "@/components/ui/button";
import { Heart } from "lucide-react";
import { wishlistApi, normalizeProperty } from "@/services/api";

function WishlistPage() {
  const { user } = useAppContext();
  const [, setLocation] = useLocation();
  const [wishlistProperties, setWishlistProperties] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) {
      // Redirect to home if they are somehow not logged in
      setLocation("/");
      return;
    }

    setLoading(true);
    wishlistApi.get()
      .then((data) => {
        const list = Array.isArray(data) ? data : data?.content || [];
        setWishlistProperties(list.map(normalizeProperty));
      })
      .catch((e) => {
        console.error("Failed to load wishlist properties", e);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [user, setLocation]);

  if (!user) return null;

  return (
    <div className="min-h-screen bg-background py-12">
      <div className="container mx-auto px-4">
        <h1 className="font-serif text-3xl font-bold mb-2">Wishlist</h1>
        <p className="text-muted-foreground mb-8">Your favorited stays and properties</p>

        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {[1, 2, 3, 4].map((n) => (
              <div key={n} className="space-y-3 animate-pulse">
                <div className="aspect-[4/3] bg-muted rounded-xl" />
                <div className="h-4 bg-muted rounded w-2/3" />
                <div className="h-4 bg-muted rounded w-1/2" />
              </div>
            ))}
          </div>
        ) : wishlistProperties.length === 0 ? (
          <div className="text-center py-24 max-w-md mx-auto border border-dashed rounded-2xl p-8 bg-card/50">
            <Heart className="w-12 h-12 text-rose-500/80 mx-auto mb-4 animate-bounce" />
            <h3 className="font-serif text-xl font-bold mb-2">No properties saved yet</h3>
            <p className="text-muted-foreground mb-6">
              When browsing, click the heart icon on properties you like to save them here.
            </p>
            <Button onClick={() => setLocation("/search")} className="rounded-xl">
              Start exploring
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {wishlistProperties.map((property) => (
              <PropertyCard key={property.id} property={property} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export { WishlistPage };
