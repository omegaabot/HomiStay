import { useState, useRef, useEffect } from "react";
import { motion } from "framer-motion";
import { X, Send, Bot, User } from "lucide-react";
import axiosInstance from "@/api/axiosInstance";

const WELCOME_MSG = {
  role: "bot",
  text: "Hi! I'm Whirly 👋. I can help you with booking properties, managing listings, account-related questions, and platform safety. How can I assist you today?",
  timestamp: new Date(),
};

function ChatPopup({ isOpen, onClose, userName }) {
  const [messages, setMessages] = useState([WELCOME_MSG]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);

  useEffect(() => {
    if (isOpen) {
      setTimeout(() => inputRef.current?.focus(), 300);
    }
  }, [isOpen]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = async () => {
    const text = input.trim();
    if (!text || loading) return;

    setMessages((prev) => [
      ...prev,
      { role: "user", text, timestamp: new Date() },
    ]);
    setInput("");
    setLoading(true);

    try {
      const { data } = await axiosInstance.post("/api/chatbot/ask", {
        question: text,
      });
      setMessages((prev) => [
        ...prev,
        {
          role: "bot",
          text: data.answer,
          timestamp: new Date(),
        },
      ]);
    } catch {
      setMessages((prev) => [
        ...prev,
        {
          role: "bot",
          text: "Sorry, I'm having trouble connecting. Please try again later.",
          timestamp: new Date(),
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20, scale: 0.95 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, y: 20, scale: 0.95 }}
      transition={{ duration: 0.2, ease: "easeOut" }}
      className="fixed bottom-24 right-6 z-50 w-[380px] max-w-[calc(100vw-3rem)] bg-white dark:bg-gray-900 rounded-2xl shadow-2xl border border-gray-200 dark:border-gray-700 flex flex-col overflow-hidden"
      style={{ maxHeight: "min(600px, calc(100vh - 180px))" }}
    >
      {/* Header */}
      <div className="bg-gradient-to-r from-teal-500 to-cyan-500 p-4 flex items-center gap-3 shrink-0">
        <div className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center">
          <Bot className="w-5 h-5 text-white" />
        </div>
        <div className="flex-1 min-w-0">
          <h3 className="text-white font-semibold text-sm">Whirly</h3>
          <p className="text-white/80 text-xs">Virtual Assistant</p>
        </div>
        <button
          onClick={onClose}
          className="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center hover:bg-white/30 transition-colors"
        >
          <X className="w-4 h-4 text-white" />
        </button>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3 bg-gray-50 dark:bg-gray-950">
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`flex ${msg.role === "user" ? "justify-end" : "justify-start"}`}
          >
            <div
              className={`max-w-[85%] rounded-2xl p-3 ${
                msg.role === "user"
                  ? "bg-teal-500 text-white rounded-br-md"
                  : "bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-bl-md"
              }`}
            >
              <div className="flex items-center gap-1.5 mb-1">
                {msg.role === "bot" ? (
                  <Bot className="w-3.5 h-3.5 text-teal-500" />
                ) : (
                  <User className="w-3.5 h-3.5 text-white/80" />
                )}
                <span className="text-xs font-medium opacity-70">
                  {msg.role === "bot" ? "Whirly" : userName || "You"}
                </span>
              </div>
              <p className="text-sm whitespace-pre-wrap leading-relaxed">
                {msg.text}
              </p>
              <p className="text-xs opacity-50 mt-1 text-right">
                {msg.timestamp.toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </p>
            </div>
          </div>
        ))}
        {loading && (
          <div className="flex justify-start">
            <div className="bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-2xl rounded-bl-md p-3">
              <div className="flex gap-1">
                <motion.div
                  className="w-2 h-2 bg-teal-500 rounded-full"
                  animate={{ y: [0, -4, 0] }}
                  transition={{ duration: 0.6, repeat: Infinity, delay: 0 }}
                />
                <motion.div
                  className="w-2 h-2 bg-teal-500 rounded-full"
                  animate={{ y: [0, -4, 0] }}
                  transition={{ duration: 0.6, repeat: Infinity, delay: 0.2 }}
                />
                <motion.div
                  className="w-2 h-2 bg-teal-500 rounded-full"
                  animate={{ y: [0, -4, 0] }}
                  transition={{ duration: 0.6, repeat: Infinity, delay: 0.4 }}
                />
              </div>
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <div className="p-3 border-t border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-900 shrink-0">
        <div className="flex items-center gap-2 bg-gray-100 dark:bg-gray-800 rounded-xl px-3 py-2">
          <input
            ref={inputRef}
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Ask me anything..."
            className="flex-1 bg-transparent text-sm outline-none placeholder-gray-400 dark:placeholder-gray-500"
            disabled={loading}
          />
          <button
            onClick={handleSend}
            disabled={!input.trim() || loading}
            className="w-8 h-8 rounded-lg bg-teal-500 text-white flex items-center justify-center hover:bg-teal-600 transition-colors disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
          >
            <Send className="w-4 h-4" />
          </button>
        </div>
      </div>
    </motion.div>
  );
}

export default ChatPopup;
