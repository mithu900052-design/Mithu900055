package com.example.data.model

enum class MonexaLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    BENGALI("bn", "বাংলা");

    companion object {
        fun fromCode(code: String): MonexaLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

object MonexaStrings {
    fun appTitle(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "MONEXA MONITOR"
        MonexaLanguage.BENGALI -> "মনেক্সা মনিটর"
    }

    fun tagLine(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Consent-Based Device Monitoring Platform"
        MonexaLanguage.BENGALI -> "সম্মতি-ভিত্তিক ডিভাইস মনিটরিং প্ল্যাটফর্ম"
    }

    fun deviceIdLabel(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Customer Device ID"
        MonexaLanguage.BENGALI -> "কাস্টমার ডিভাইস আইডি"
    }

    fun copyButton(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Copy Device ID"
        MonexaLanguage.BENGALI -> "ডিভাইস আইডি কপি করুন"
    }

    fun copiedToast(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Device ID copied to clipboard"
        MonexaLanguage.BENGALI -> "ডিভাইস আইডি ক্লিপবোর্ডে কপি করা হয়েছে"
    }

    fun pairingStatus(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Pairing Status"
        MonexaLanguage.BENGALI -> "পেয়ারিং স্ট্যাটাস"
    }

    fun statusUnpaired(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Unpaired (Ready to connect)"
        MonexaLanguage.BENGALI -> "আনপেয়ার্ড (সংযোগের জন্য প্রস্তুত)"
    }

    fun statusPending(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Pending Admin Approval"
        MonexaLanguage.BENGALI -> "অ্যাডমিন অনুমোদনের অপেক্ষায়"
    }

    fun statusPaired(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Securely Paired with Admin"
        MonexaLanguage.BENGALI -> "অ্যাডমিনের সাথে সুরক্ষিতভাবে সংযুক্ত"
    }

    fun statusRevoked(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Access Revoked by Customer"
        MonexaLanguage.BENGALI -> "কাস্টমার দ্বারা অ্যাক্সেস প্রত্যাহার করা হয়েছে"
    }

    fun telemetryOverview(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Live Device Telemetry"
        MonexaLanguage.BENGALI -> "লাইভ ডিভাইস টেলিমেট্রি"
    }

    fun battery(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Battery"
        MonexaLanguage.BENGALI -> "ব্যাটারি"
    }

    fun charging(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Charging"
        MonexaLanguage.BENGALI -> "চার্জ হচ্ছে"
    }

    fun discharging(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "On Battery"
        MonexaLanguage.BENGALI -> "ব্যাটারিতে চলছে"
    }

    fun storage(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Free Storage"
        MonexaLanguage.BENGALI -> "ফাঁকা স্টোরেজ"
    }

    fun network(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Network"
        MonexaLanguage.BENGALI -> "নেটওয়ার্ক"
    }

    fun osVersion(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Android OS"
        MonexaLanguage.BENGALI -> "অ্যান্ড্রয়েড সংস্করণ"
    }

    fun privacyConsentHeader(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Privacy & Consent Safeguards"
        MonexaLanguage.BENGALI -> "গোপনীয়তা ও সম্মতির সুরক্ষা"
    }

    fun privacyConsentDesc(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "MONEXA operates strictly on verified customer consent. No secret monitoring, no hidden tracking, and no unauthorized access. You can revoke admin access at any second."
        MonexaLanguage.BENGALI -> "মনেক্সা কঠোরভাবে যাচাইকৃত কাস্টমার সম্মতিতে কাজ করে। কোনো গোপন মনিটরিং বা অননুমোদিত অ্যাক্সেস নেই। আপনি যেকোনো মুহূর্তে অ্যাক্সেস প্রত্যাহার করতে পারবেন।"
    }

    fun phase1Notice(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Phase 1: Project Foundation & Architecture Active"
        MonexaLanguage.BENGALI -> "পর্যায় ১: প্রজেক্ট ফাউন্ডেশন ও আর্কিটেকচার সক্রিয়"
    }

    fun approve(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Approve Pairing"
        MonexaLanguage.BENGALI -> "অনুমোদন করুন"
    }

    fun reject(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Reject"
        MonexaLanguage.BENGALI -> "প্রত্যাখ্যান করুন"
    }

    fun revoke(lang: MonexaLanguage) = when (lang) {
        MonexaLanguage.ENGLISH -> "Revoke Admin Access"
        MonexaLanguage.BENGALI -> "অ্যাডমিন অ্যাক্সেস প্রত্যাহার করুন"
    }
}
