import React, { useState } from 'react';
import { Shield, Send, X, AlertTriangle, CheckCircle2 } from 'lucide-react';
import { sendPairingRequest } from '../services/firebase';

interface PairingModalProps {
  isOpen: boolean;
  onClose: () => void;
  language: 'en' | 'bn';
  onPairingSent: (deviceId: string) => void;
}

export const PairingModal: React.FC<PairingModalProps> = ({
  isOpen,
  onClose,
  language,
  onPairingSent,
}) => {
  const [deviceIdInput, setDeviceIdInput] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // Only allow digits, max 6 characters
    const clean = e.target.value.replace(/\D/g, '').slice(0, 6);
    setDeviceIdInput(clean);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (deviceIdInput.length !== 6) {
      setErrorMessage(
        language === 'en'
          ? 'Device ID must be exactly 6 numerical digits.'
          : 'ডিভাইস আইডি অবশ্যই ঠিক ৬টি সংখ্যা হতে হবে।'
      );
      return;
    }

    setIsSubmitting(true);
    setErrorMessage(null);

    try {
      await sendPairingRequest(deviceIdInput);
      setSuccessMessage(
        language === 'en'
          ? `Pairing request dispatched to Device ${deviceIdInput}. Waiting for customer approval.`
          : `ডিভাইস ${deviceIdInput}-এ পেয়ারিং অনুরোধ পাঠানো হয়েছে। গ্রাহকের অনুমোদনের অপেক্ষায় রয়েছে।`
      );
      onPairingSent(deviceIdInput);
      setTimeout(() => {
        setIsSubmitting(false);
        setSuccessMessage(null);
        setDeviceIdInput('');
        onClose();
      }, 2000);
    } catch (err: any) {
      setErrorMessage(err.message || 'Failed to dispatch pairing request');
      setIsSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/75 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-monexa-surface border border-monexa-border rounded-2xl max-w-md w-full p-6 shadow-2xl relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-slate-400 hover:text-white p-1 rounded-lg hover:bg-monexa-surfaceLight transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-3 mb-4">
          <div className="w-10 h-10 rounded-xl bg-monexa-cyan/20 border border-monexa-cyan/50 flex items-center justify-center">
            <Shield className="w-5 h-5 text-monexa-cyan" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-white">
              {language === 'en' ? 'Pair New Customer Device' : 'নতুন কাস্টমার ডিভাইস পেয়ার করুন'}
            </h3>
            <p className="text-xs text-slate-400">
              {language === 'en' ? 'Consent-Based 6-Digit Handshake' : 'সম্মতি-ভিত্তিক ৬-সংখ্যার হ্যান্ডশেক'}
            </p>
          </div>
        </div>

        {/* Security Warning Notice */}
        <div className="bg-monexa-surfaceLight/60 border border-monexa-border rounded-xl p-3.5 mb-5 flex items-start gap-2.5">
          <AlertTriangle className="w-4 h-4 text-monexa-amber shrink-0 mt-0.5" />
          <p className="text-xs text-slate-300 leading-relaxed">
            {language === 'en'
              ? 'Security Rule: A Device ID alone never grants telemetry access. The device customer must explicitly tap "Approve" on their phone.'
              : 'নিরাপত্তা বিধি: শুধুমাত্র ডিভাইস আইডি কোনো তথ্য দেখায় না। গ্রাহককে অবশ্যই তার ফোনে "অনুমোদন" বোতাম চাপতে হবে।'}
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-2">
              {language === 'en' ? 'Enter 6-Digit Numeric Device ID' : '৬-সংখ্যার ডিভাইস আইডি প্রবেশ করান'}
            </label>
            <div className="relative">
              <input
                type="text"
                value={deviceIdInput}
                onChange={handleInputChange}
                placeholder="e.g. 827491"
                maxLength={6}
                disabled={isSubmitting}
                className="w-full bg-monexa-dark border border-monexa-border focus:border-monexa-cyan rounded-xl px-4 py-3 text-2xl font-mono tracking-widest text-center text-monexa-cyan placeholder:text-slate-600 focus:outline-none transition-colors"
              />
            </div>
            <p className="text-[11px] text-slate-400 mt-1.5 text-center">
              {language === 'en'
                ? 'Generated on the customer\'s Monexa app'
                : 'গ্রাহকের মনেক্সা অ্যাপে প্রদর্শিত আইডি'}
            </p>
          </div>

          {errorMessage && (
            <div className="p-3 rounded-lg bg-red-950/60 border border-red-800 text-xs text-red-300">
              {errorMessage}
            </div>
          )}

          {successMessage && (
            <div className="p-3 rounded-lg bg-emerald-950/60 border border-emerald-800 text-xs text-emerald-300 flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 shrink-0 text-monexa-emerald" />
              <span>{successMessage}</span>
            </div>
          )}

          <div className="flex gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              disabled={isSubmitting}
              className="flex-1 px-4 py-2.5 rounded-xl border border-monexa-border hover:bg-monexa-surfaceLight text-sm font-semibold text-slate-300 transition-colors"
            >
              {language === 'en' ? 'Cancel' : 'বাতিল'}
            </button>
            <button
              type="submit"
              disabled={isSubmitting || deviceIdInput.length !== 6}
              className="flex-1 px-4 py-2.5 rounded-xl bg-monexa-cyan hover:bg-cyan-400 disabled:opacity-50 disabled:cursor-not-allowed text-sm font-bold text-slate-950 flex items-center justify-center gap-2 transition-colors shadow-lg shadow-monexa-cyan/20"
            >
              <Send className="w-4 h-4" />
              <span>{isSubmitting ? (language === 'en' ? 'Sending...' : 'পাঠানো হচ্ছে...') : (language === 'en' ? 'Send Pairing Request' : 'পেয়ারিং অনুরোধ পাঠান')}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
