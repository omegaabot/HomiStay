import { useState, useEffect } from "react";
import { useLocation } from "wouter";
import { useAppContext } from "@/context/AppContext";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  MessageSquare,
  AlertCircle,
  CheckCircle2,
  Clock,
  Search,
  RefreshCw,
  Bot,
  Ticket,
} from "lucide-react";
import { supportApi } from "@/services/api";

function SupportDashboard() {
  const { user } = useAppContext();
  const [, setLocation] = useLocation();
  const [stats, setStats] = useState(null);
  const [queries, setQueries] = useState({ content: [], totalElements: 0, totalPages: 0, page: 0 });
  const [tickets, setTickets] = useState({ content: [], totalElements: 0, totalPages: 0, page: 0 });
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [tab, setTab] = useState("dashboard");
  const [statusFilter, setStatusFilter] = useState("");

  const isSupport = user?.role === "support_team" || user?.role === "admin";

  useEffect(() => {
    if (!isSupport) return;
    loadData();
  }, [isSupport, tab, statusFilter]);

  const loadData = async () => {
    setLoading(true);
    try {
      if (tab === "dashboard") {
        const dashData = await supportApi.dashboard();
        setStats(dashData);
      } else if (tab === "queries") {
        const params = { page: queries.page, size: 10 };
        if (searchQuery) params.search = searchQuery;
        const data = await supportApi.getQueries(params);
        setQueries(data);
      } else if (tab === "tickets") {
        const params = { page: tickets.page, size: 10 };
        if (searchQuery) params.search = searchQuery;
        if (statusFilter) params.status = statusFilter;
        const data = await supportApi.getTickets(params);
        setTickets(data);
      }
    } catch {
      // handle error
    } finally {
      setLoading(false);
    }
  };

  if (!isSupport) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h2 className="font-serif text-2xl font-bold mb-4">Support Team access required</h2>
        <Button onClick={() => setLocation("/")}>Go home</Button>
      </div>
    );
  }

  const statusColors = {
    OPEN: "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200",
    IN_PROGRESS: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
    RESOLVED: "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200",
    CLOSED: "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200",
  };

  const tabs = [
    { id: "dashboard", label: "Dashboard", icon: Bot },
    { id: "queries", label: "Chat Queries", icon: MessageSquare },
    { id: "tickets", label: "Support Tickets", icon: Ticket },
  ];

  return (
    <div className="min-h-screen bg-background py-8">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between mb-8 flex-wrap gap-4">
          <div>
            <h1 className="font-serif text-3xl font-bold">Support Dashboard</h1>
            <p className="text-muted-foreground mt-1">Manage chatbot queries and support tickets</p>
          </div>
          <div className="flex gap-2">
            {tabs.map((t) => (
              <Button
                key={t.id}
                variant={tab === t.id ? "default" : "outline"}
                size="sm"
                onClick={() => { setTab(t.id); setSearchQuery(""); setStatusFilter(""); }}
                className="gap-2"
              >
                <t.icon className="w-4 h-4" />
                {t.label}
              </Button>
            ))}
          </div>
        </div>

        {tab === "dashboard" && stats && (
          <>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
              {[
                { label: "Total Queries", value: stats.totalQueries, icon: MessageSquare, color: "text-blue-600" },
                { label: "Open Issues", value: stats.openIssues, icon: AlertCircle, color: "text-amber-600" },
                { label: "Resolved Issues", value: stats.resolvedIssues, icon: CheckCircle2, color: "text-green-600" },
                { label: "Pending Issues", value: stats.pendingIssues, icon: Clock, color: "text-purple-600" },
              ].map((stat) => (
                <div key={stat.label} className="bg-card border rounded-2xl p-5">
                  <stat.icon className={`w-5 h-5 mb-3 ${stat.color}`} />
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <p className="text-sm font-medium mt-0.5">{stat.label}</p>
                </div>
              ))}
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Recent Queries */}
              <div className="bg-card border rounded-2xl p-6">
                <h2 className="font-serif text-lg font-semibold mb-4">Recent Queries</h2>
                {stats.recentQueries?.length === 0 ? (
                  <p className="text-muted-foreground text-sm">No queries yet.</p>
                ) : (
                  <div className="space-y-3">
                    {stats.recentQueries?.slice(0, 5).map((q) => (
                      <div key={q.id} className="p-3 rounded-xl bg-muted/50">
                        <p className="text-sm font-medium truncate">{q.question}</p>
                        <p className="text-xs text-muted-foreground mt-1">{q.userName} &middot; {new Date(q.createdAt).toLocaleDateString()}</p>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* Recent Tickets */}
              <div className="bg-card border rounded-2xl p-6">
                <h2 className="font-serif text-lg font-semibold mb-4">Recent Tickets</h2>
                {stats.recentTickets?.length === 0 ? (
                  <p className="text-muted-foreground text-sm">No tickets yet.</p>
                ) : (
                  <div className="space-y-3">
                    {stats.recentTickets?.slice(0, 5).map((t) => (
                      <div key={t.id} className="p-3 rounded-xl bg-muted/50">
                        <div className="flex items-center justify-between">
                          <p className="text-sm font-medium truncate">{t.issueType}</p>
                          <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[t.status] || ""}`}>
                            {t.status}
                          </span>
                        </div>
                        <p className="text-xs text-muted-foreground mt-1">{t.userName} &middot; {new Date(t.createdAt).toLocaleDateString()}</p>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </>
        )}

        {tab === "queries" && (
          <div className="bg-card border rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-6">
              <div className="relative flex-1 max-w-md">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder="Search queries..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && loadData()}
                  className="pl-9 rounded-xl"
                />
              </div>
              <Button variant="outline" size="icon" onClick={loadData} className="rounded-xl">
                <RefreshCw className="w-4 h-4" />
              </Button>
            </div>

            {loading ? (
              <p className="text-muted-foreground text-center py-8">Loading...</p>
            ) : queries.content?.length === 0 ? (
              <p className="text-muted-foreground text-center py-8">No queries found.</p>
            ) : (
              <div className="space-y-3">
                {queries.content?.map((q) => (
                  <div key={q.id} className="p-4 rounded-xl border bg-card hover:bg-muted/30 transition-colors">
                    <div className="flex items-start justify-between gap-4">
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium">{q.question}</p>
                        <p className="text-xs text-muted-foreground mt-1 line-clamp-2">{q.chatbotResponse}</p>
                      </div>
                      <div className="text-right shrink-0">
                        <p className="text-xs text-muted-foreground">{q.userName || "Anonymous"}</p>
                        <p className="text-xs text-muted-foreground">{new Date(q.createdAt).toLocaleDateString()}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {tab === "tickets" && (
          <div className="bg-card border rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-6 flex-wrap">
              <div className="relative flex-1 max-w-md">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder="Search tickets..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && loadData()}
                  className="pl-9 rounded-xl"
                />
              </div>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="rounded-xl border border-input bg-background px-3 py-2 text-sm"
              >
                <option value="">All Status</option>
                <option value="OPEN">Open</option>
                <option value="IN_PROGRESS">Open - In Progress</option>
                <option value="RESOLVED">Resolved</option>
                <option value="CLOSED">Closed</option>
              </select>
              <Button variant="outline" size="icon" onClick={loadData} className="rounded-xl">
                <RefreshCw className="w-4 h-4" />
              </Button>
            </div>

            {loading ? (
              <p className="text-muted-foreground text-center py-8">Loading...</p>
            ) : tickets.content?.length === 0 ? (
              <p className="text-muted-foreground text-center py-8">No tickets found.</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b text-left">
                      <th className="pb-3 font-medium text-muted-foreground">Ticket ID</th>
                      <th className="pb-3 font-medium text-muted-foreground">User</th>
                      <th className="pb-3 font-medium text-muted-foreground">Issue Type</th>
                      <th className="pb-3 font-medium text-muted-foreground">Status</th>
                      <th className="pb-3 font-medium text-muted-foreground">Created</th>
                    </tr>
                  </thead>
                  <tbody>
                    {tickets.content?.map((t) => (
                      <tr key={t.id} className="border-b last:border-0 hover:bg-muted/30 transition-colors">
                        <td className="py-3 font-mono text-xs">#{t.id}</td>
                        <td className="py-3">
                          <p className="font-medium">{t.userName}</p>
                          <p className="text-xs text-muted-foreground">{t.userEmail}</p>
                        </td>
                        <td className="py-3 capitalize">{t.issueType.replace(/_/g, " ").toLowerCase()}</td>
                        <td className="py-3">
                          <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[t.status] || ""}`}>
                            {t.status}
                          </span>
                        </td>
                        <td className="py-3 text-muted-foreground text-xs">
                          {new Date(t.createdAt).toLocaleDateString()}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export { SupportDashboard };
