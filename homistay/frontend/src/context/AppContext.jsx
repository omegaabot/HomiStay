import { createContext, useContext, useState, useEffect, useCallback } from "react";
import { authApi, propertiesApi, wishlistApi, normalizeProperty, normalizeUser } from "../services/api";

const AppContext = createContext(undefined);

function AppProvider({ children }) {
  const [user, setUser] = useState(null);
  const [properties, setProperties] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [currentBooking, setCurrentBooking] = useState(null);
  const [authLoading, setAuthLoading] = useState(true);
  const [wishlist, setWishlist] = useState([]);

  // Load wishlist when user changes
  useEffect(() => {
    if (user) {
      wishlistApi.get()
        .then((res) => {
          const list = Array.isArray(res) ? res : res?.content || [];
          setWishlist(list.map(p => String(p.id)));
        })
        .catch(() => setWishlist([]));
    } else {
      setWishlist([]);
    }
  }, [user]);

  // Restore session from localStorage on mount
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      setAuthLoading(false);
      return;
    }
    authApi
      .me()
      .then((userData) => setUser(normalizeUser(userData)))
      .catch(() => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("role");
      })
      .finally(() => setAuthLoading(false));
  }, []);

  // Load properties from backend on mount
  useEffect(() => {
    propertiesApi
      .search({ size: 50 })
      .then((res) => {
        // Spring Page: { content: [...] } or plain array
        const list = Array.isArray(res) ? res : res?.content;
        if (list?.length) {
          setProperties(list.map(normalizeProperty));
        } else {
          setProperties([]);
        }
      })
      .catch(() => {
        setProperties([]);
      });
  }, []);

  // Listen for 401 / token expiry events from the Axios interceptor
  useEffect(() => {
    const handleLogout = () => setUser(null);
    window.addEventListener("auth:logout", handleLogout);
    return () => window.removeEventListener("auth:logout", handleLogout);
  }, []);

  const login = useCallback((userData) => {
    setUser(userData);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role"); // clear old key left by earlier integrations
    setUser(null);
    window.location.href = "/";
  }, []);

  const toggleWishlist = useCallback(async (propertyId) => {
    if (!user) {
      window.dispatchEvent(new CustomEvent("open-auth", { detail: { mode: "login" } }));
      return;
    }
    if (user?.role === "host" || user?.role === "admin") {
      return;
    }
    const propIdStr = String(propertyId);
    const isSaved = wishlist.includes(propIdStr);
    try {
      if (isSaved) {
        await wishlistApi.remove(propertyId);
        setWishlist(prev => prev.filter(id => id !== propIdStr));
      } else {
        await wishlistApi.add(propertyId);
        setWishlist(prev => [...prev, propIdStr]);
      }
    } catch (e) {
      console.error("Failed to toggle wishlist", e);
    }
  }, [user, wishlist]);

  return (
    <AppContext.Provider
      value={{
        user,
        setUser,
        properties,
        setProperties,
        bookings,
        setBookings,
        currentBooking,
        setCurrentBooking,
        isAuthenticated: !!user,
        authLoading,
        login,
        logout,
        wishlist,
        toggleWishlist,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

function useAppContext() {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error("useAppContext must be used within an AppProvider");
  }
  return context;
}

export { AppProvider, useAppContext };
