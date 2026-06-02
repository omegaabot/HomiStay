import { useState } from "react";
import { useLocation } from "wouter";
import { useAppContext } from "@/context/AppContext";
import { Button } from "@/components/ui/button";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";
import { DollarSign, TrendingUp, CreditCard, ArrowUpRight } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

const mockEarningsData = [
  { month: "Jan", amount: 2100 },
  { month: "Feb", amount: 3400 },
  { month: "Mar", amount: 2800 },
  { month: "Apr", amount: 4200 },
  { month: "May", amount: 5100 },
  { month: "Jun", amount: 6300 },
  { month: "Jul", amount: 7800 },
  { month: "Aug", amount: 8200 },
  { month: "Sep", amount: 5900 },
  { month: "Oct", amount: 4400 },
  { month: "Nov", amount: 3200 },
  { month: "Dec", amount: 4800 }
];

function HostEarningsPage() {
  const { user, bookings, properties } = useAppContext();
  const [, setLocation] = useLocation();
  const { toast } = useToast();

  const thisMonthEarnings = mockEarningsData[4].amount;
  const [payouts, setPayouts] = useState([
    { date: "May 1, 2026", amount: 4200, method: "Bank transfer \u2022\u2022\u2022\u2022 4242", status: "Paid" },
    { date: "Apr 1, 2026", amount: 3800, method: "Bank transfer \u2022\u2022\u2022\u2022 4242", status: "Paid" },
    { date: "Mar 1, 2026", amount: 2800, method: "Bank transfer \u2022\u2022\u2022\u2022 4242", status: "Paid" },
    { date: "Jun 1, 2026", amount: thisMonthEarnings, method: "Bank transfer \u2022\u2022\u2022\u2022 4242", status: "Pending" }
  ]);
  const [isProcessing, setIsProcessing] = useState(false);

  const handleRequestPayout = () => {
    const pendingPayout = payouts.find((p) => p.status === "Pending");
    if (!pendingPayout) return;

    setIsProcessing(true);
    setTimeout(() => {
      setPayouts((prev) =>
        prev.map((p) => (p.status === "Pending" ? { ...p, status: "Paid" } : p))
      );
      toast({
        title: "Payout Initiated",
        description: `Early payout of $${pendingPayout.amount.toLocaleString()} has been sent to your bank account successfully!`,
      });
      setIsProcessing(false);
    }, 1500);
  };

  if (!user || user.role !== "host") {
    return <div className="container mx-auto px-4 py-20 text-center">
        <h2 className="font-serif text-2xl font-bold mb-4">Host access required</h2>
        <Button onClick={() => setLocation("/")}>Go home</Button>
      </div>;
  }
  const hostProperties = properties.filter((p) => p.hostId === user.id);
  const hostBookings = bookings.filter((b) => hostProperties.some((p) => p.id === b.propertyId));
  const totalEarnings = mockEarningsData.reduce((sum, m) => sum + m.amount, 0);
  const lastMonthEarnings = mockEarningsData[3].amount;
  const growthPct = ((thisMonthEarnings - lastMonthEarnings) / lastMonthEarnings * 100).toFixed(1);
  return <div className="min-h-screen bg-background py-8">
      <div className="container mx-auto px-4">
        <div className="mb-8">
          <h1 className="font-serif text-3xl font-bold">Earnings</h1>
          <p className="text-muted-foreground mt-1">Track your revenue and payouts</p>
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
          {[
    { label: "Total Earnings", value: `$${totalEarnings.toLocaleString()}`, icon: DollarSign, sub: "All time", color: "text-green-600" },
    { label: "This Month", value: `$${thisMonthEarnings.toLocaleString()}`, icon: TrendingUp, sub: `+${growthPct}% vs last month`, color: "text-blue-600" },
    { label: "Confirmed Bookings", value: hostBookings.filter((b) => b.status === "confirmed").length, icon: CreditCard, sub: "Revenue generating", color: "text-primary" },
    { label: "Avg per Booking", value: `$${hostBookings.length > 0 ? Math.round(hostBookings.reduce((s, b) => s + b.totalPrice, 0) / hostBookings.length).toLocaleString() : 0}`, icon: ArrowUpRight, sub: "Average booking value", color: "text-amber-600" }
  ].map((stat) => <div key={stat.label} className="bg-card border rounded-2xl p-5">
              <stat.icon className={`w-5 h-5 ${stat.color} mb-3`} />
              <p className="text-2xl font-bold">{stat.value}</p>
              <p className="text-sm font-medium text-foreground mt-0.5">{stat.label}</p>
              <p className="text-xs text-muted-foreground mt-0.5">{stat.sub}</p>
            </div>)}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {
    /* Chart */
  }
          <div className="lg:col-span-2 bg-card border rounded-2xl p-6">
            <h2 className="font-serif text-xl font-semibold mb-2">Monthly Earnings</h2>
            <p className="text-sm text-muted-foreground mb-6">Revenue by month — 2026</p>
            <ResponsiveContainer width="100%" height={280}>
              <BarChart data={mockEarningsData} barSize={28}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-border" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} className="text-muted-foreground" />
                <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `$${v / 1e3}k`} className="text-muted-foreground" />
                <Tooltip
    formatter={(value) => [`$${value.toLocaleString()}`, "Earnings"]}
    contentStyle={{ borderRadius: "12px", border: "1px solid hsl(var(--border))", background: "hsl(var(--card))" }}
  />
                <Bar dataKey="amount" fill="hsl(var(--primary))" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {
    /* Payout History */
  }
          <div className="bg-card border rounded-2xl p-6">
            <h2 className="font-serif text-xl font-semibold mb-5">Payout History</h2>
            <div className="space-y-4">
              {payouts.map((payout, i) => <div key={i} className="flex items-center justify-between gap-3" data-testid={`payout-row-${i}`}>
                  <div className="min-w-0">
                    <p className="text-sm font-medium">{payout.date}</p>
                    <p className="text-xs text-muted-foreground truncate">{payout.method}</p>
                  </div>
                  <div className="text-right shrink-0">
                    <p className="font-semibold text-sm">${payout.amount.toLocaleString()}</p>
                    <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${payout.status === "Paid" ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200" : "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200"}`}>
                      {payout.status}
                    </span>
                  </div>
                </div>)}
            </div>
            {(() => {
              const hasPending = payouts.some((p) => p.status === "Pending");
              return (
                <Button
                  variant="outline"
                  className="w-full mt-5 rounded-xl"
                  data-testid="button-request-payout"
                  disabled={isProcessing || !hasPending}
                  onClick={handleRequestPayout}
                >
                  {isProcessing ? "Processing..." : hasPending ? "Request early payout" : "No pending payouts"}
                </Button>
              );
            })()}
          </div>
        </div>

        {
    /* Breakdown table */
  }
        <div className="mt-8 bg-card border rounded-2xl overflow-hidden shadow-sm">
          <div className="p-5 border-b">
            <h2 className="font-serif text-xl font-semibold">Earnings Breakdown</h2>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="bg-muted/30 border-b">
                  <th className="text-left p-4 text-sm font-semibold text-muted-foreground">Month</th>
                  <th className="text-right p-4 text-sm font-semibold text-muted-foreground">Gross</th>
                  <th className="text-right p-4 text-sm font-semibold text-muted-foreground">Service Fee (3%)</th>
                  <th className="text-right p-4 text-sm font-semibold text-muted-foreground">Net Payout</th>
                </tr>
              </thead>
              <tbody className="divide-y">
                {mockEarningsData.slice().reverse().map((row, i) => <tr key={i} className="hover:bg-muted/30 transition-colors">
                    <td className="p-4 text-sm font-medium">{row.month} 2026</td>
                    <td className="p-4 text-sm text-right">${row.amount.toLocaleString()}</td>
                    <td className="p-4 text-sm text-right text-muted-foreground">-${Math.round(row.amount * 0.03).toLocaleString()}</td>
                    <td className="p-4 text-sm text-right font-semibold">${Math.round(row.amount * 0.97).toLocaleString()}</td>
                  </tr>)}
              </tbody>
              <tfoot>
                <tr className="bg-muted/30 border-t">
                  <td className="p-4 font-semibold">Total</td>
                  <td className="p-4 font-semibold text-right">${totalEarnings.toLocaleString()}</td>
                  <td className="p-4 font-semibold text-right text-muted-foreground">-${Math.round(totalEarnings * 0.03).toLocaleString()}</td>
                  <td className="p-4 font-semibold text-right">${Math.round(totalEarnings * 0.97).toLocaleString()}</td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
      </div>
    </div>;
}
export {
  HostEarningsPage
};
