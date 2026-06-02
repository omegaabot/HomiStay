import { Switch, Route, Router as WouterRouter } from "wouter";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "@/components/ui/toaster.jsx";
import { TooltipProvider } from "@/components/ui/tooltip.jsx";
import NotFound from "@/pages/not-found.jsx";
import { AppProvider } from "@/context/AppContext.jsx";
import { Navbar } from "@/components/Navbar.jsx";
import { HomePage } from "@/pages/Home.jsx";
import { SearchPage } from "@/pages/Search.jsx";
import { PropertyDetailsPage } from "@/pages/PropertyDetails.jsx";
import { CheckoutPage } from "@/pages/Checkout.jsx";
import { ConfirmationPage } from "@/pages/Confirmation.jsx";
import { MyBookingsPage } from "@/pages/MyBookings.jsx";
import { ProfilePage } from "@/pages/Profile.jsx";
import { WishlistPage } from "@/pages/Wishlist.jsx";
import { HostDashboard } from "@/pages/host/Dashboard.jsx";
import { HostPropertiesPage } from "@/pages/host/Properties.jsx";
import { HostBookingsPage } from "@/pages/host/Bookings.jsx";
import { HostEarningsPage } from "@/pages/host/Earnings.jsx";
import { HostCalendarPage } from "@/pages/host/Calendar.jsx";
import { AddPropertyPage } from "@/pages/host/AddProperty.jsx";
import { HostPricingPage } from "@/pages/host/Pricing.jsx";
import { SupportDashboard } from "@/pages/SupportDashboard.jsx";
import WhirlyCharacter from "@/components/Whirly.jsx";

const queryClient = new QueryClient();

function Router() {
  return (
    <Switch>
      <Route path="/" component={HomePage} />
      <Route path="/search" component={SearchPage} />
      <Route path="/property/:id" component={PropertyDetailsPage} />
      <Route path="/checkout" component={CheckoutPage} />
      <Route path="/confirmation" component={ConfirmationPage} />
      <Route path="/my-bookings" component={MyBookingsPage} />
      <Route path="/profile" component={ProfilePage} />
      <Route path="/wishlist" component={WishlistPage} />


      <Route path="/host/dashboard" component={HostDashboard} />
      <Route path="/host/properties" component={HostPropertiesPage} />
      <Route path="/host/bookings" component={HostBookingsPage} />
      <Route path="/host/earnings" component={HostEarningsPage} />
      <Route path="/host/calendar" component={HostCalendarPage} />
      <Route path="/host/pricing" component={HostPricingPage} />
      <Route path="/host/add-property" component={AddPropertyPage} />
      <Route path="/host/edit-property/:id" component={AddPropertyPage} />

      <Route path="/support/dashboard" component={SupportDashboard} />

      <Route component={NotFound} />
    </Switch>
  );
}

function AppContent() {
  return (
    <>
      <div className="min-h-screen bg-background text-foreground flex flex-col">
        <Navbar />
        <main className="flex-1 flex flex-col">
          <Router />
        </main>
      </div>
      <WhirlyCharacter />
    </>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <AppProvider>
          <WouterRouter base={import.meta.env.BASE_URL.replace(/\/$/, "")}>
            <AppContent />
          </WouterRouter>
          <Toaster />
        </AppProvider>
      </TooltipProvider>
    </QueryClientProvider>
  );
}

export default App;
