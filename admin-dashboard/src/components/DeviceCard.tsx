import React from 'react';
import { Smartphone, BatteryCharging, Battery, HardDrive, Wifi, ShieldCheck, ShieldAlert, ArrowRight, Clock } from 'lucide-react';
import { Device } from '../types/monexa';

interface DeviceCardProps {
  device: Device;
  language: 'en' | 'bn';
  onSelect: (device: Device) => void;
}

export const DeviceCard: React.FC<DeviceCardProps> = ({ device, language, onSelect }) => {
  const getStatusBadge = () => {
    switch (device.pairingStatus) {
      case 'PAIRED':
        return (
          <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-emerald-950/60 border border-emerald-800 text-[11px] font-semibold text-emerald-400">
            <ShieldCheck className="w-3 h-3" />
            {language === 'en' ? 'PAIRED & ACTIVE' : 'সংযুক্ত ও সক্রিয়'}
          </span>
        );
      case 'PENDING_APPROVAL':
        return (
          <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-amber-950/60 border border-amber-800 text-[11px] font-semibold text-amber-400">
            <Clock className="w-3 h-3" />
            {language === 'en' ? 'PENDING APPROVAL' : 'অনুমোদনের অপেক্ষায়'}
          </span>
        );
      case 'REVOKED':
        return (
          <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-red-950/60 border border-red-800 text-[11px] font-semibold text-red-400">
            <ShieldAlert className="w-3 h-3" />
            {language === 'en' ? 'ACCESS REVOKED' : 'প্রত্যাহার করা হয়েছে'}
          </span>
        );
      default:
        return (
          <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-slate-800 border border-slate-700 text-[11px] font-semibold text-slate-300">
            {language === 'en' ? 'UNPAIRED' : 'আনপেয়ার্ড'}
          </span>
        );
    }
  };

  return (
    <div className="bg-monexa-surface border border-monexa-border hover:border-monexa-cyan/60 rounded-2xl p-5 transition-all duration-300 shadow-xl group hover:shadow-monexa-cyan/5 flex flex-col justify-between">
      <div>
        {/* Top bar: Device ID & Pairing status */}
        <div className="flex items-start justify-between gap-3 mb-3.5">
          <div className="flex items-center gap-2.5">
            <div className="w-9 h-9 rounded-xl bg-monexa-cyan/10 border border-monexa-cyan/30 flex items-center justify-center text-monexa-cyan">
              <Smartphone className="w-5 h-5" />
            </div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="font-mono text-lg font-black tracking-widest text-monexa-cyan">
                  {device.deviceId}
                </span>
                <span className={`w-2 h-2 rounded-full ${device.isOnline ? 'bg-monexa-emerald animate-ping' : 'bg-slate-600'}`}></span>
              </div>
              <p className="text-xs text-slate-300 font-medium">
                {device.customerName} {device.customerAge ? `(${device.customerAge} yrs)` : ''}
              </p>
            </div>
          </div>
          <div>{getStatusBadge()}</div>
        </div>

        {/* Hardware details */}
        <div className="bg-monexa-dark/60 rounded-xl p-3 border border-monexa-border/60 mb-4 space-y-1.5">
          <div className="flex justify-between text-xs">
            <span className="text-slate-400">{language === 'en' ? 'Model' : 'মডেল'}:</span>
            <span className="text-slate-200 font-medium">{device.model || 'Android Device'}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-slate-400">{language === 'en' ? 'OS' : 'অপারেটিং সিস্টেম'}:</span>
            <span className="text-slate-300 font-mono text-[11px]">{device.androidVersion || 'Android 14'}</span>
          </div>
        </div>

        {/* Real-time telemetry grid */}
        <div className="grid grid-cols-3 gap-2 mb-4">
          {/* Battery */}
          <div className="bg-monexa-surfaceLight/40 rounded-lg p-2 border border-monexa-border/40 text-center">
            <div className="flex items-center justify-center gap-1 text-slate-400 mb-1">
              {device.isCharging ? (
                <BatteryCharging className="w-3.5 h-3.5 text-monexa-cyan" />
              ) : (
                <Battery className="w-3.5 h-3.5 text-slate-400" />
              )}
              <span className="text-[10px] uppercase font-bold">{language === 'en' ? 'Battery' : 'ব্যাটারি'}</span>
            </div>
            <p className="text-xs font-mono font-bold text-slate-200">{device.batteryPercentage}%</p>
          </div>

          {/* Storage */}
          <div className="bg-monexa-surfaceLight/40 rounded-lg p-2 border border-monexa-border/40 text-center">
            <div className="flex items-center justify-center gap-1 text-slate-400 mb-1">
              <HardDrive className="w-3.5 h-3.5 text-monexa-blue" />
              <span className="text-[10px] uppercase font-bold">{language === 'en' ? 'Free' : 'ফাঁকা'}</span>
            </div>
            <p className="text-xs font-mono font-bold text-slate-200">{device.availableStorageGb} GB</p>
          </div>

          {/* Network */}
          <div className="bg-monexa-surfaceLight/40 rounded-lg p-2 border border-monexa-border/40 text-center">
            <div className="flex items-center justify-center gap-1 text-slate-400 mb-1">
              <Wifi className="w-3.5 h-3.5 text-monexa-emerald" />
              <span className="text-[10px] uppercase font-bold">{language === 'en' ? 'Net' : 'নেট'}</span>
            </div>
            <p className="text-xs font-mono font-bold text-slate-200 truncate">{device.networkType || 'Wi-Fi'}</p>
          </div>
        </div>
      </div>

      {/* Action Footer */}
      <button
        onClick={() => onSelect(device)}
        className="w-full py-2.5 px-4 rounded-xl bg-monexa-surfaceLight hover:bg-monexa-cyan hover:text-slate-950 border border-monexa-border hover:border-monexa-cyan text-xs font-bold text-slate-200 flex items-center justify-center gap-2 transition-all group-hover:bg-monexa-cyan group-hover:text-slate-950 shadow-md"
      >
        <span>{language === 'en' ? 'Open Device Control Center' : 'ডিভাইস কন্ট্রোল সেন্টার খুলুন'}</span>
        <ArrowRight className="w-3.5 h-3.5" />
      </button>
    </div>
  );
};
