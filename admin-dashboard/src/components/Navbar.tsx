import React, { useState, useEffect } from 'react';
import { Shield, Radio, Globe, ShieldAlert, Cpu } from 'lucide-react';

interface NavbarProps {
  language: 'en' | 'bn';
  onLanguageToggle: () => void;
  onlineCount: number;
}

export const Navbar: React.FC<NavbarProps> = ({ language, onLanguageToggle, onlineCount }) => {
  const [time, setTime] = useState<string>('');

  useEffect(() => {
    const update = () => {
      const now = new Date();
      setTime(now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }));
    };
    update();
    const timer = setInterval(update, 1000);
    return () => clearInterval(timer);
  }, []);

  return (
    <header className="sticky top-0 z-40 bg-monexa-dark/80 backdrop-blur-md border-b border-monexa-border px-6 py-3.5 flex items-center justify-between">
      <div className="flex items-center gap-3.5">
        <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-monexa-cyan/20 to-monexa-blue/30 border border-monexa-cyan/50 flex items-center justify-center shadow-lg shadow-monexa-cyan/10">
          <Shield className="w-5 h-5 text-monexa-cyan" />
        </div>
        <div>
          <div className="flex items-center gap-2">
            <span className="font-extrabold text-base tracking-wider text-white">
              MONEXA <span className="text-monexa-cyan">MONITOR</span>
            </span>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-monexa-surfaceLight border border-monexa-border text-slate-300">
              SECURE ADMIN v1.0
            </span>
          </div>
          <p className="text-xs text-slate-400">
            {language === 'en' ? 'Enterprise Consent-Based Telemetry Control' : 'এন্টারপ্রাইজ সম্মতি-ভিত্তিক টেলিমেট্রি কন্ট্রোল'}
          </p>
        </div>
      </div>

      <div className="flex items-center gap-4">
        {/* Real-time telemetry connection beacon */}
        <div className="hidden md:flex items-center gap-2 px-3 py-1.5 rounded-lg bg-monexa-surface border border-monexa-border">
          <Radio className="w-3.5 h-3.5 text-monexa-emerald animate-pulse" />
          <span className="text-xs text-slate-300">
            {onlineCount} {language === 'en' ? 'Active Nodes' : 'সক্রিয় ডিভাইস'}
          </span>
          <span className="text-slate-600 font-mono">|</span>
          <span className="text-xs font-mono text-monexa-cyan">{time}</span>
        </div>

        {/* Language switch */}
        <button
          onClick={onLanguageToggle}
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-monexa-surface hover:bg-monexa-surfaceLight border border-monexa-border text-xs font-semibold text-slate-200 transition-colors"
        >
          <Globe className="w-3.5 h-3.5 text-monexa-cyan" />
          <span>{language === 'en' ? 'বাংলা' : 'English'}</span>
        </button>

        {/* Admin credential badge */}
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-gradient-to-r from-blue-950/60 to-slate-900 border border-blue-800/40">
          <div className="w-2 h-2 rounded-full bg-monexa-emerald"></div>
          <span className="text-xs font-medium text-slate-200">SecOps Admin</span>
        </div>
      </div>
    </header>
  );
};
