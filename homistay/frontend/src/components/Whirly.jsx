import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useAppContext } from "@/context/AppContext";
import ChatPopup from "./ChatPopup";

function WhirlyCharacter() {
  const [isOpen, setIsOpen] = useState(false);
  const [blink, setBlink] = useState(false);
  const { user } = useAppContext();

  useEffect(() => {
    const blinkInterval = setInterval(() => {
      setBlink(true);
      setTimeout(() => setBlink(false), 150);
    }, 4000);
    return () => clearInterval(blinkInterval);
  }, []);

  const handleToggle = () => setIsOpen((prev) => !prev);

  return (
    <>
      <AnimatePresence>
        {isOpen && (
          <ChatPopup
            isOpen={isOpen}
            onClose={() => setIsOpen(false)}
            userName={user?.name}
          />
        )}
      </AnimatePresence>

      <motion.button
        onClick={handleToggle}
        className="fixed bottom-6 right-6 z-50 cursor-pointer focus:outline-none"
        animate={{
          y: [0, -8, 0],
        }}
        transition={{
          duration: 3,
          repeat: Infinity,
          ease: "easeInOut",
        }}
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.95 }}
        aria-label="Open Whirly chatbot"
      >
        <div className="relative">
          <svg width="72" height="72" viewBox="0 0 72 72" fill="none" xmlns="http://www.w3.org/2000/svg">
            {/* Shadow */}
            <ellipse cx="36" cy="66" rx="18" ry="4" fill="rgba(0,0,0,0.08)" />
            {/* Body */}
            <circle cx="36" cy="36" r="30" fill="#4FD1C5" />
            <circle cx="36" cy="36" r="28" fill="url(#whirlyGrad)" />
            {/* Belly highlight */}
            <ellipse cx="36" cy="46" rx="16" ry="12" fill="rgba(255,255,255,0.25)" />
            {/* Left eye */}
            <motion.g
              animate={blink ? { scaleY: 0 } : { scaleY: 1 }}
              transition={{ duration: 0.1 }}
              style={{ originX: 27, originY: 32 }}
            >
              <ellipse cx="27" cy="32" rx="5" ry="6" fill="#1A202C" />
              <circle cx="28" cy="30" r="2" fill="white" />
            </motion.g>
            {/* Right eye */}
            <motion.g
              animate={blink ? { scaleY: 0 } : { scaleY: 1 }}
              transition={{ duration: 0.1 }}
              style={{ originX: 45, originY: 32 }}
            >
              <ellipse cx="45" cy="32" rx="5" ry="6" fill="#1A202C" />
              <circle cx="46" cy="30" r="2" fill="white" />
            </motion.g>
            {/* Blush */}
            <ellipse cx="18" cy="40" rx="4" ry="3" fill="rgba(255,107,107,0.3)" />
            <ellipse cx="54" cy="40" rx="4" ry="3" fill="rgba(255,107,107,0.3)" />
            {/* Mouth - gentle smile */}
            <motion.path
              d="M30 44 Q36 50 42 44"
              stroke="#1A202C"
              strokeWidth="2"
              strokeLinecap="round"
              fill="none"
            />
            {/* Small sparkle */}
            <motion.circle
              cx="54"
              cy="18"
              r="2"
              fill="white"
              animate={{ opacity: [0, 1, 0] }}
              transition={{ duration: 2, repeat: Infinity, delay: 1 }}
            />
            <defs>
              <linearGradient id="whirlyGrad" x1="12" y1="12" x2="60" y2="60">
                <stop stopColor="#81E6D9" />
                <stop offset="1" stopColor="#319795" />
              </linearGradient>
            </defs>
          </svg>
          {/* Sparkles */}
          <motion.div
            className="absolute -top-1 -right-1 text-lg"
            animate={{ rotate: [0, 15, -15, 0], scale: [1, 1.2, 1] }}
            transition={{ duration: 4, repeat: Infinity }}
          >
            ✨
          </motion.div>
        </div>
      </motion.button>
    </>
  );
}

export default WhirlyCharacter;
