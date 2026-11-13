import React, { useState } from 'react';
import { 
  ArrowLeft, 
  Smartphone, 
  Info, 
  MapPin, 
  History, 
  Layers, 
  Clock, 
  Bell, 
  Image, 
  Tv, 
  FileText, 
  ShieldCheck, 
  Activity, 
  BatteryCharging, 
  HardDrive, 
  Wifi, 
  LinkOff,
  AlertTriangle,
  RefreshCw,
  CheckCircle2
} from 'lucide-react';
import { Device } from '../types/monexa';
import { LiveScreenViewer } from '../components/LiveScreenViewer';
import { revokeDeviceAccess } from '../services/firebase';

interface DeviceDetailPageProps {
  device: Device;
  language: 'en' | 'bn';
  onBack: () => void;
  onRefresh: () => void;
}

export const DeviceDetailPage: React.FC<DeviceDetailPageProps> = ({
  device,
  language,
  onBack,
  onRefresh
}) => {
  const [activeTab, setActiveTab] = useState<string>('overview');
  const [isRevoking, setIsRevoking] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);

  const tabs = [
    { id: 'overview', label: language === 'en' ? 'Overview' : 'সারসংক্ষেপ', icon: Info },
    { id: 'device_info', label: language === 'en' ? 'Device Info' : 'ডিভাইস তথ্য', icon: Smartphone },
    { id: 'live_location', label: language === 'en' ? 'Live Location' : 'লাইভ লোকেশন', icon: MapPin },
    { id: 'location_history', label: language === 'en' ? 'Location History' : 'লোকেশন ইতিহাস', icon: History },
    { id: 'app_usage', label: language === 'en' ? 'App Usage' : 'অ্যাপ ব্যবহার', icon: Layers },
    { id: 'screen_time', label: language === 'en' ? 'Screen Time' : 'স্ক্রিন সময়', icon: Clock },
    { id: 'notifications', label: language === 'en' ? 'Notifications' : 'নোটিফিকেশন', icon: Bell },
    { id: 'shared_media', label: language === 'en' ? 'Shared Media' : 'শেয়ার্ড মিডিয়া', icon: Image },
    { id: 'screen_viewer', label: language === 'en' ? 'Screen Viewer' : 'স্ক্রিন ভিউয়ার', icon: Tv },
    { id: 'reports', label: language === 'en' ? 'Reports' : 'রিপোর্ট', icon: FileText },
    { id: 'privacy_permissions', label: language === 'en' ? 'Privacy & Permissions' : 'অনুমতি ও সুরক্ষা', icon: ShieldCheck },
    { id: 'connection_history', label: language === 'en' ? 'Connection Logs' : 'সংযোগ লগ', icon: Activity },
  ];

  const handleRevoke = async () => {
    if (!window.confirm(language === 'en' ? 'Revoke admin pairing access for this device?' : 'এই ডিভাইসের অ্যাডমিন সংযোগ প্রত্যাহার করবেন?')) return;
    setIsRevoking(true);
    try {
      await revokeDeviceAccess(device.deviceId);
      setFeedback(language === 'en' ? 'Access revoked successfully' : 'অ্যাক্সেস সফলভাবে প্রত্যাহার করা হয়েছে');
      onRefresh();
    } catch (e: any) {
      alert(e.message);
    } finally {
      setIsRevoking(false);
    }
  };

  return (
    <div className="p-6 max-w-7xl mx-auto space-y-6">
      {/* Navigation Header */}
      <div className="flex items-center justify-between flex-wrap gap-4">
        <div className="flex items-center gap-3">
          <button
            onClick={onBack}
            className="p-2 rounded-xl bg-monexa-surface hover:bg-monexa-surfaceLight border border-monexa-border text-slate-300 hover:text-white transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-mono text-xl font-black text-monexa-cyan tracking-widest">
                ID: {device.deviceId}
              </span>
              <span className="text-xs px-2.5 py-0.5 rounded-full bg-emerald-950/80 border border-emerald-800 text-emerald-400 font-mono font-semibold">
                {device.pairingStatus}
              </span>
            </div>
            <p className="text-xs text-slate-400">
              {device.customerName} • {device.model || 'Android Device'}
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={onRefresh}
            className="p-2 rounded-xl bg-monexa-surface hover:bg-monexa-surfaceLight border border-monexa-border text-slate-300 hover:text-white transition-colors"
            title="Refresh"
          >
            <RefreshCw className="w-4 h-4" />
          </button>

          {device.pairingStatus === 'PAIRED' && (
            <button
              onClick={handleRevoke}
              disabled={isRevoking}
              className="px-3.5 py-2 rounded-xl bg-red-950/60 hover:bg-red-900/60 border border-red-800 text-red-300 text-xs font-bold flex items-center gap-1.5 transition-colors"
            >
              <LinkOff className="w-3.5 h-3.5" />
              <span>{isRevoking ? 'Revoking...' : (language === 'en' ? 'Revoke Access' : 'সংযোগ বাতিল করুন')}</span>
            </button>
          )}
        </div>
      </div>

      {feedback && (
        <div className="p-3 rounded-xl bg-emerald-950/60 border border-emerald-800 text-emerald-300 text-xs flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4" />
          <span>{feedback}</span>
        </div>
      )}

      {/* 12-Section Tab Navigation Strip */}
      <div className="bg-monexa-surface border border-monexa-border rounded-2xl p-1.5 flex items-center gap-1 overflow-x-auto">
        {tabs.map((tab) => {
          const IconComp = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`flex items-center gap-2 px-3.5 py-2 rounded-xl text-xs font-semibold whitespace-nowrap transition-all ${
                isActive
                  ? 'bg-monexa-cyan text-slate-950 shadow-md shadow-monexa-cyan/20 font-bold'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-monexa-surfaceLight'
              }`}
            >
              <IconComp className="w-4 h-4 shrink-0" />
              <span>{tab.label}</span>
            </button>
          );
        })}
      </div>

      {/* Tab Panels Content */}
      <div className="bg-monexa-surface border border-monexa-border rounded-2xl p-6 min-h-[460px]">
        {/* Tab 1: Overview */}
        {activeTab === 'overview' && (
          <div className="space-y-6">
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <Info className="w-4 h-4 text-monexa-cyan" />
              <span>{language === 'en' ? 'Device Telemetry Snapshot' : 'ডিভাইস টেলিমেট্রি সারসংক্ষেপ'}</span>
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-monexa-dark/60 border border-monexa-border rounded-xl p-4">
                <div className="flex items-center gap-2 text-slate-400 mb-1">
                  <BatteryCharging className="w-4 h-4 text-monexa-cyan" />
                  <span className="text-xs">{language === 'en' ? 'Battery Level' : 'ব্যাটারি লেভেল'}</span>
                </div>
                <p className="text-2xl font-mono font-bold text-white">{device.batteryPercentage}%</p>
                <p className="text-[11px] text-slate-500 mt-1">{device.isCharging ? 'Plugged In (Charging)' : 'Discharging'}</p>
              </div>

              <div className="bg-monexa-dark/60 border border-monexa-border rounded-xl p-4">
                <div className="flex items-center gap-2 text-slate-400 mb-1">
                  <HardDrive className="w-4 h-4 text-monexa-blue" />
                  <span className="text-xs">{language === 'en' ? 'Storage Available' : 'ফাঁকা স্টোরেজ'}</span>
                </div>
                <p className="text-2xl font-mono font-bold text-white">{device.availableStorageGb} GB</p>
                <p className="text-[11px] text-slate-500 mt-1">Total {device.totalStorageGb} GB</p>
              </div>

              <div className="bg-monexa-dark/60 border border-monexa-border rounded-xl p-4">
                <div className="flex items-center gap-2 text-slate-400 mb-1">
                  <Wifi className="w-4 h-4 text-monexa-emerald" />
                  <span className="text-xs">{language === 'en' ? 'Network' : 'নেটওয়ার্ক'}</span>
                </div>
                <p className="text-xl font-mono font-bold text-white truncate">{device.networkType || 'Wi-Fi'}</p>
                <p className="text-[11px] text-slate-500 mt-1">{device.isOnline ? 'Online (Real-Time)' : 'Offline'}</p>
              </div>

              <div className="bg-monexa-dark/60 border border-monexa-border rounded-xl p-4">
                <div className="flex items-center gap-2 text-slate-400 mb-1">
                  <Smartphone className="w-4 h-4 text-monexa-amber" />
                  <span className="text-xs">{language === 'en' ? 'Android OS' : 'অ্যান্ড্রয়েড ওএস'}</span>
                </div>
                <p className="text-lg font-mono font-bold text-white">{device.androidVersion || 'Android 14'}</p>
                <p className="text-[11px] text-slate-500 mt-1">{device.model || 'Model Info'}</p>
              </div>
            </div>

            <div className="bg-monexa-surfaceLight/40 border border-monexa-border rounded-xl p-4">
              <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-2">
                {language === 'en' ? 'Consent Status & Permission Matrix' : 'সম্মতি স্ট্যাটাস ও অনুমতি ম্যাট্রিক্স'}
              </h4>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3 text-xs">
                <div className="flex items-center gap-2">
                  <span className={`w-2 h-2 rounded-full ${device.locationSharingEnabled ? 'bg-monexa-emerald' : 'bg-slate-600'}`}></span>
                  <span className="text-slate-300">Location Sharing</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className={`w-2 h-2 rounded-full ${device.screenSharingEnabled ? 'bg-monexa-emerald' : 'bg-slate-600'}`}></span>
                  <span className="text-slate-300">Live Screen Sharing</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className={`w-2 h-2 rounded-full ${device.usageAccessGranted ? 'bg-monexa-emerald' : 'bg-slate-600'}`}></span>
                  <span className="text-slate-300">Usage Access</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full bg-monexa-emerald"></span>
                  <span className="text-slate-300">Telemetry Sync</span>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Tab 2: Device Information */}
        {activeTab === 'device_info' && (
          <div className="space-y-4">
            <h3 className="text-base font-bold text-white">{language === 'en' ? 'Detailed Hardware Telemetry' : 'বিস্তারিত হার্ডওয়্যার টেলিমেট্রি'}</h3>
            <div className="bg-monexa-dark/60 rounded-xl border border-monexa-border p-4 divide-y divide-monexa-border/40">
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Device 6-Digit Identifier:</span>
                <span className="font-mono text-monexa-cyan font-bold">{device.deviceId}</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Device Model:</span>
                <span className="text-slate-200">{device.model || 'Unknown Model'}</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Manufacturer:</span>
                <span className="text-slate-200">{device.manufacturer || 'Android OEM'}</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">OS Version:</span>
                <span className="text-slate-200">{device.androidVersion || 'Android'}</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Monexa App Version:</span>
                <span className="text-slate-200">{device.appVersion || '1.0.0'}</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Battery Level & State:</span>
                <span className="text-slate-200">{device.batteryPercentage}% ({device.isCharging ? 'Charging' : 'On Battery'})</span>
              </div>
              <div className="py-2.5 flex justify-between text-xs">
                <span className="text-slate-400">Internal Storage Allocation:</span>
                <span className="text-slate-200">{device.availableStorageGb} GB Free / {device.totalStorageGb} GB Total</span>
              </div>
            </div>
          </div>
        )}

        {/* Tab 9: Live Screen Viewer */}
        {activeTab === 'screen_viewer' && (
          <div className="space-y-4">
            <LiveScreenViewer device={device} language={language} />
          </div>
        )}

        {/* Other Tabs: Clean Placeholder & Module Information */}
        {activeTab !== 'overview' && activeTab !== 'device_info' && activeTab !== 'screen_viewer' && (
          <div className="space-y-4 text-center py-12 max-w-md mx-auto">
            <div className="w-12 h-12 rounded-xl bg-monexa-cyan/10 border border-monexa-cyan/30 flex items-center justify-center mx-auto text-monexa-cyan">
              <ShieldCheck className="w-6 h-6" />
            </div>
            <h4 className="text-base font-bold text-white capitalize">
              {activeTab.replace('_', ' ')}
            </h4>
            <p className="text-xs text-slate-400 leading-relaxed">
              {language === 'en'
                ? 'Consent-gated module configured in Firestore security rules. Activates when customer grants respective Android runtime permission in later phases.'
                : 'সম্মতি-ভিত্তিক মডিউল যা ফায়ারস্টোর সিকিউরিটি রুলে কনফিগার করা হয়েছে। গ্রাহক অনুমতি প্রদান করলে এটি সক্রিয় হবে।'}
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
