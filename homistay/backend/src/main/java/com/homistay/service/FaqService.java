package com.homistay.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FaqService {

    private final Map<Pattern, String> faqPatterns = new LinkedHashMap<>();

    public FaqService() {
        // ── ── Booking a Property ──────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(book|reserve|bookings|reservation)\\b.*\\b(how|can|property|place)\\b.*"),
            "1. Browse available properties.\n2. Open the property details page.\n3. Select your check-in and check-out dates.\n4. Enter guest information.\n5. Complete the booking process.\n6. You'll receive a booking confirmation after successful payment."
        );

        // ── ── Cancel Booking ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(cancel|cancellation|delete|modify|change)\\b.*\\b(bookings|reservation)\\b.*"),
            "1. Go to My Bookings.\n2. Select the booking you wish to cancel.\n3. Open booking details.\n4. Click Cancel Booking.\n5. Follow the confirmation process."
        );

        // ── ── Modify Booking ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(modify|change|update|edit|reschedule)\\b.*\\b(booking|reservation|dates|check)\\b.*"),
            "Currently, you need to cancel the existing booking and create a new one with the updated details. Go to My Bookings, cancel the booking, and rebook with your preferred dates."
        );

        // ── ── See My Bookings ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(how\\s*to\\s*see|view|find|show|where)\\b.*\\b(my\\s*bookings|my\\s*reservations|booking\\s*history)\\b.*|(?i).*\\bhow\\s*(can\\s*I\\s*)?(view|check|see)\\b.*\\b(booking|reservation)s?\\b.*"),
            "1. Click the profile icon in the top-right corner.\n2. Select 'My Bookings' from the menu.\n3. You'll see all your upcoming and past reservations.\n4. Click any booking to view full details."
        );

        // ── ── Booking Not Visible ────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(booking\\s*not\\s*show|can'?t\\s*see\\b.*\\b(booking|reservation)|reservation\\s*not\\s*visible|missing\\s*booking)\\b.*"),
            "Please refresh the page and check your My Bookings section. If the issue persists, contact the support team."
        );

        // ── ── Login ──────────────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(how\\s*to\\s*log|log\\s*in|login|sign\\s*in|signin)\\b.*"),
            "1. Click the profile icon button in the top-right corner.\n2. Select 'Log in' from the dropdown.\n3. Enter your email and password.\n4. Click 'Log in' to access your account.\n\nIf you don't have an account, click 'Sign up' to register."
        );

        // ── ── Register / Sign Up ─────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(register|sign\\s*up|create\\s*account|new\\s*account|join)\\b.*"),
            "1. Click the profile icon button in the top-right corner.\n2. Select 'Sign up'.\n3. Enter your full name, email, and password.\n4. Optionally check 'Sign up as a Host' to list properties.\n5. Click 'Sign up' to create your account.\n\nYou'll be logged in automatically after registration."
        );

        // ── ── Forgot / Reset Password ───────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(forgot|reset|change|recover)\\b.*\\b(password)\\b.*"),
            "If you've forgotten your password, please contact our support team at support@homistay.com for assistance with resetting your account password."
        );

        // ── ── Update Profile ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(update|edit|change|modify)\\b.*\\b(profile|name|phone|bio|avatar|picture)\\b.*|(?i).*\\bhow\\s*to\\s*(update|edit|change)\\b.*\\b(profile|account)\\b.*"),
            "1. Click the profile icon in the top-right corner.\n2. Select 'Profile' from the menu.\n3. Update your name, phone, bio, or profile picture.\n4. Click Save to apply the changes."
        );

        // ── ── Delete Account ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(delete|close|remove)\\b.*\\b(account)\\b.*"),
            "To delete your account, please contact our support team at support@homistay.com. We'll assist you with the account deletion process."
        );

        // ── ── Search & Filter Properties ──────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(how\\s*to\\s*search|find|look\\s*for|browse)\\b.*\\b(property|home|place|listing)\\b.*|(?i).*\\bhow\\s*(can\\s*I\\s*)?(search|filter)\\b.*"),
            "1. Use the search bar at the top of the page.\n2. Enter a destination, city, or property name.\n3. Use filters to narrow by price, bedrooms, amenities, and more.\n4. Browse the results and click any property for details."
        );

        // ── ── Property Details ───────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(see|view|show)\\b.*\\b(details|info|information|description)\\b.*\\b(property|home|place)\\b.*|(?i).*\\bhow\\s*to\\s*(see|view|open)\\b.*\\b(property\\s*details)\\b.*"),
            "Simply click on any property card from the search results or home page. This will open the property details page showing photos, description, amenities, pricing, reviews, and location."
        );

        // ── ── Like / Save / Wishlist ───────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(like|save|favourite|favorite|wishlist|heart|add\\s*to\\s*wishlist|remove\\s*from\\s*wishlist|unlike|unsave)\\b.*\\b(property|home|listing)\\b.*|(?i).*\\bhow\\s*to\\s*(like|save|favourite|favorite|unlike|unsave)\\b.*"),
            "1. Open the property you're interested in.\n2. Click the heart icon on the property card or details page.\n3. The property will be saved to your Wishlist.\n4. Access your Wishlist anytime from the user menu in the top-right corner.\n5. Click the heart icon again to remove from your Wishlist."
        );

        // ── ── View Wishlist ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(view|see|open|access)\\b.*\\b(wishlist|saved|favourite|favorite)\\b.*|(?i).*\\bwhere\\s*(is|are)\\b.*\\b(wishlist|saved|favourite)\\b.*"),
            "1. Click the profile icon in the top-right corner.\n2. Select 'Wishlist' from the menu.\n3. You'll see all properties you've saved.\n4. Click any property to view details or book it."
        );

        // ── ── Payment Methods ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(payment\\s*method|pay\\s*with|what\\s*payment|accepted\\s*payment|credit\\s*card|debit\\s*card)\\b.*"),
            "We accept major credit and debit cards for payments. All transactions are processed securely. Make sure your payment details are correct before confirming your booking."
        );

        // ── ── Refund ────────────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(refund|money\\s*back|get\\s*back|reimburse)\\b.*"),
            "Refunds are processed based on the cancellation policy of the property. Please check your booking details or contact our support team at support@homistay.com for refund assistance."
        );

        // ── ── Payment Failed ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(payment\\s*fail|pay\\s*issue|payment\\s*problem|can'?t\\s*pay|transaction\\s*fail|payment\\s*error|payment\\s*declined)\\b.*"),
            "Please verify your payment method and try again. If the issue continues, contact our support team."
        );

        // ── ── List Property / Become Host ──────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(list|add)\\b.*\\b(property|home|listing)\\b.*|(?i).*\\b(become|host)\\b.*"),
            "1. Login as a host.\n2. Open Host Dashboard.\n3. Click Add Property.\n4. Fill in property details.\n5. Upload property images.\n6. Submit for approval."
        );

        // ── ── Delete Property Listing ───────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(delete|remove)\\b.*\\b(property|listing)\\b.*"),
            "1. Open Host Dashboard.\n2. Navigate to My Properties.\n3. Select the property.\n4. Click Delete Property.\n5. Confirm deletion."
        );

        // ── ── Host Dashboard ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(host\\s*dashboard|manage\\s*property|host\\s*panel)\\b.*"),
            "1. Login with your host account.\n2. Click the profile icon in the top-right corner.\n3. Select 'Dashboard' from the menu.\n4. From there you can manage properties, view bookings, track earnings, and more."
        );

        // ── ── Write a Review ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(leave|write|give|post|submit)\\b.*\\b(review|rating)\\b.*|(?i).*\\bhow\\s*(can\\s*I\\s*)?(review|rate)\\b.*"),
            "1. Go to My Bookings.\n2. Find a completed booking.\n3. Open the booking details.\n4. Click 'Write a Review'.\n5. Rate the property and leave your comments.\n6. Submit your review."
        );

        // ── ── View Reviews ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(see|view|read|check)\\b.*\\b(review|ratings|feedback)\\b.*\\b(property)\\b.*|(?i).*\\bhow\\s*(are|can|do)\\b.*\\b(review|ratings)\\b.*"),
            "Open any property details page and scroll down to the Reviews section. You'll see ratings and feedback left by previous guests."
        );

        // ── ── Contact Support ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(contact|reach|talk\\s*to|get\\s*help|help\\s*desk|customer\\s*service)\\b.*\\b(support|team|you|us)\\b.*|(?i).*\\bhow\\s*(can\\s*I\\s*)?(contact|reach)\\b.*"),
            "You can reach our support team via email at support@homistay.com. We'll get back to you as soon as possible."
        );

        // ── ── Report an Issue ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(report|complaint|issue|problem)\\b.*\\b(host|property|booking|user|payment)\\b.*|(?i).*\\bhow\\s*(can\\s*I\\s*)?report\\b.*"),
            "If you're experiencing an issue, please contact our support team at support@homistay.com with the details. Include your booking ID if applicable, and we'll assist you promptly."
        );

        // ── ── Platform Safety ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(safe|secure|data\\s*protect|privacy|protected|is\\s*this\\s*safe|trust)\\b.*"),
            "Our platform uses secure authentication, protected APIs, access control mechanisms, and data validation processes to help keep user information secure. Always use strong passwords and avoid sharing your account credentials."
        );

        // ── ── What is Homistay ────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(what\\s*is|tell\\s*me\\s*about|about)\\b.*\\b(homistay|this\\s*platform|this\\s*site|your\\s*service)\\b.*"),
            "Homistay is a platform that connects travelers with unique accommodations. Guests can browse and book properties, while hosts can list their spaces and earn income. We aim to provide a safe and seamless experience for all users."
        );

        // ── ── How it Works ──────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\bhow\\s*(does|do)\\s*(this|you|it)\\s*work|how\\s*the\\s*platform\\s*works|how\\s*does\\s*homistay\\b.*"),
            "Homistay works in three simple steps:\n1. Guests browse and book properties.\n2. Hosts list their spaces and manage reservations.\n3. Both parties communicate through the platform.\n\nSign up for free to get started!"
        );

        // ── ── Pricing & Fees ─────────────────────────────────────────────────────────
        faqPatterns.put(
            Pattern.compile("(?i).*\\b(prices|pricing|fees|charges|cost|how\\s*much)\\b.*\\b(booking|stay|per\\s*night|cleaning|service)\\b.*|(?i).*\\b(cleaning\\s*fee|service\\s*fee|what\\s*are\\s*the\\s*fees)\\b.*"),
            "Property prices are set by the host per night. Additional charges may include cleaning fees and applicable taxes. The total price breakdown is shown before you confirm your booking."
        );
    }

    public Optional<String> findAnswer(String question) {
        return faqPatterns.entrySet().stream()
            .filter(e -> e.getKey().matcher(question).matches())
            .map(Map.Entry::getValue)
            .findFirst();
    }
}
