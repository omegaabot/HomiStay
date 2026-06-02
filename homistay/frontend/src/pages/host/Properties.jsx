import { useState, useEffect } from "react";
import { Link, useLocation } from "wouter";
import { useAppContext } from "@/context/AppContext";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { StarRating } from "@/components/StarRating";
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter,
} from "@/components/ui/dialog";
import { Plus, Eye, Trash2, ToggleLeft, ToggleRight, Edit3 } from "lucide-react";
import { hostApi, normalizeProperty, propertiesApi } from "@/services/api";

const statusColors = {
  active: "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200",
  inactive: "bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-200",
};

function HostPropertiesPage() {
  const { user } = useAppContext();
  const [, setLocation] = useLocation();
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState("all");
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const isHost = user?.role === "host" || user?.role === "admin";

  useEffect(() => {
    if (!isHost) return;
    hostApi.myProperties()
      .then(({ content }) => { if (content) setProperties(content.map(normalizeProperty)); })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [isHost]);

  if (!isHost) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h2 className="font-serif text-2xl font-bold mb-4">Host access required</h2>
        <Button onClick={() => setLocation("/")}>Go home</Button>
      </div>
    );
  }

  const filtered = statusFilter === "all" ? properties : properties.filter((p) => p.status === statusFilter);

  const toggleStatus = async (prop) => {
    const newActive = prop.status !== "active";
    try {
      await propertiesApi.update(prop.id, {
        title: prop.title, description: prop.description,
        type: prop.type.toUpperCase(), city: prop.location.city,
        country: prop.location.country, address: prop.location.address || "",
        pricePerNight: prop.price, maxGuests: prop.maxGuests,
        bedrooms: prop.bedrooms, bathrooms: prop.bathrooms,
        amenities: (prop.amenities || []).join(","),
        isActive: newActive,
      });
      setProperties((prev) => prev.map((p) =>
        p.id === prop.id ? { ...p, status: newActive ? "active" : "inactive" } : p
      ));
    } catch {
      // Revert on failure
    }
  };

  const { setProperties: setGlobalProperties } = useAppContext();

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    setDeleting(true);
    try {
      await propertiesApi.delete(deleteTarget);
      setProperties((prev) => prev.filter((p) => p.id !== deleteTarget));
      setGlobalProperties((prev) => prev.filter((p) => p.id !== deleteTarget));
    } catch (err) {
      alert(err.response?.data?.message || "Failed to delete property.");
    } finally {
      setDeleting(false);
      setDeleteTarget(null);
    }
  };

  return (
    <div className="min-h-screen bg-background py-8">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between mb-8 flex-wrap gap-4">
          <div>
            <h1 className="font-serif text-3xl font-bold">My Properties</h1>
            <p className="text-muted-foreground mt-1">{properties.length} properties total</p>
          </div>
          <div className="flex items-center gap-3">
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-36 rounded-full">
                <SelectValue placeholder="Filter" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All statuses</SelectItem>
                <SelectItem value="active">Active</SelectItem>
                <SelectItem value="inactive">Inactive</SelectItem>
              </SelectContent>
            </Select>
            <Link href="/host/add-property">
              <Button className="gap-2 rounded-full"><Plus className="w-4 h-4" /> Add property</Button>
            </Link>
          </div>
        </div>

        {loading ? (
          <div className="text-center py-20 text-muted-foreground">Loading properties…</div>
        ) : filtered.length === 0 ? (
          <div className="text-center py-20">
            <p className="text-xl font-serif font-bold mb-2">No properties yet</p>
            <p className="text-muted-foreground mb-6">Start earning by listing your first property.</p>
            <Link href="/host/add-property">
              <Button className="gap-2"><Plus className="w-4 h-4" /> List your first property</Button>
            </Link>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            {filtered.map((property) => (
              <div key={property.id} className="bg-card border rounded-2xl overflow-hidden shadow-sm hover:shadow-md transition-shadow">
                <div className="relative aspect-[16/9]">
                  <img src={property.images[0]} alt={property.title} className="w-full h-full object-cover" />
                  <div className="absolute top-3 left-3">
                    <span className={`text-xs font-semibold px-2 py-1 rounded-full ${statusColors[property.status] || ""}`}>
                      {property.status}
                    </span>
                  </div>
                </div>
                <div className="p-5">
                  <h3 className="font-semibold leading-snug mb-1 line-clamp-1">{property.title}</h3>
                  <p className="text-sm text-muted-foreground mb-2">{property.location.city}, {property.location.country}</p>
                  <div className="flex items-center justify-between mb-4">
                    <StarRating rating={property.rating} showNumber count={property.reviewCount} />
                    <span className="font-semibold">${property.price}<span className="text-muted-foreground font-normal text-sm">/night</span></span>
                  </div>
                  <div className="flex gap-2">
                    <Link href={`/property/${property.id}`} className="flex-1">
                      <Button variant="outline" size="sm" className="w-full gap-1.5 rounded-lg">
                        <Eye className="w-3.5 h-3.5" /> View
                      </Button>
                    </Link>
                    <Link href={`/host/edit-property/${property.id}`}>
                      <Button variant="outline" size="sm" className="rounded-lg gap-1.5">
                        <Edit3 className="w-3.5 h-3.5" />
                      </Button>
                    </Link>
                    <Button variant="outline" size="sm" className="rounded-lg gap-1.5" onClick={() => toggleStatus(property)}>
                      {property.status === "active"
                        ? <ToggleRight className="w-3.5 h-3.5 text-green-600" />
                        : <ToggleLeft className="w-3.5 h-3.5 text-muted-foreground" />}
                    </Button>
                    <Button variant="outline" size="sm" className="rounded-lg text-destructive hover:text-destructive hover:bg-destructive/5"
                      onClick={() => setDeleteTarget(property.id)}>
                      <Trash2 className="w-3.5 h-3.5" />
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Dialog open={!!deleteTarget} onOpenChange={(o) => { if (!o) setDeleteTarget(null); }}>
        <DialogContent className="sm:max-w-md rounded-2xl">
          <DialogHeader>
            <DialogTitle className="font-serif text-xl">Delete property</DialogTitle>
            <DialogDescription className="text-muted-foreground">
              Are you sure you want to delete this property? This action will immediately
              deactivate it and it will no longer be visible to guests. You cannot undo this.
            </DialogDescription>
          </DialogHeader>
          <DialogFooter className="flex gap-3 sm:justify-end">
            <Button variant="outline" onClick={() => setDeleteTarget(null)}>Cancel</Button>
            <Button variant="destructive" onClick={confirmDelete} disabled={deleting}>
              {deleting ? "Deleting…" : "Delete permanently"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

export { HostPropertiesPage };
