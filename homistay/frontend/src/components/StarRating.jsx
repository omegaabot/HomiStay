import { Star } from "lucide-react";
function StarRating({ rating, count, className = "" }) {
  return <div className={`flex items-center gap-1 ${className}`} data-testid="star-rating">
      <Star className="w-4 h-4 fill-primary text-primary" />
      <span className="font-medium text-sm">{rating.toFixed(1)}</span>
      {count !== void 0 && <span className="text-muted-foreground text-sm">({count})</span>}
    </div>;
}
export {
  StarRating
};
