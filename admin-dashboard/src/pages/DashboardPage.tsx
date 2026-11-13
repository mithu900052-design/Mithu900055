import React, { useState } from 'react';
import { 
  Smartphone, 
  Wifi, 
  WifiOff, 
  Clock, 
  Tv, 
  Plus, 
  Search, 
  Filter, 
  ShieldCheck, 
  Sparkles,
  RefreshCw
} from 'lucide-react';
import { Device, PairingRequest } from '../types/monexa';
import { DeviceCard } from '../components/DeviceCard';
import { PairingModal } from '../components/PairingModal';

interface DashboardPageProps {
  devices: Device[];
  pairingRequests: PairingRequest[];
  language: 'en' | 'bn';
  onSelectDevice: (device: Device) => void;
  onRefresh: () => void;
}

export const DashboardPage: React.FC<DashboardPageProps> = ({
  devices,
  pairingRequests,
  language,
  onSelectDevice,
  onRefresh,
}) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('ALL');
  const [isPairingModalOpen, setIsPairingModalOpen] = useState(false);

  // Computed metrics
  const totalCount = devices.length;
  const onlineCount = devices.filter(d => d.isOnline).length;
  const offlineCount = devices.filter(d => !d.isOnline).length;
  const pendingCount = pairingRequests.filter(r => r.status === 'PENDING').length;
  const activeScreenSessions = devices.filter(d => d.screenSharingEnabled).length;

  // Filtered devices
  const filteredDevices = devices.filter(d => {
    const matchesSearch = 
      d.deviceId.includes(searchTerm) ||
      d.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.model.toLowerCase().includes(searchTerm.toLowerCase());
    
    if (!matchesSearch) return false;

    if (filterStatus === 'PAIRED') return d.pairingStatus === 'PAIRED';
    if (filterStatus === 'PENDING') return d.pairingStatus === 'PENDING_APPROVAL';
    if (filterStatus === 'ONLINE') return d.isOnline;
    return true;
  });

  return (
    <div className="p-6 max-w-7xl mx-auto space-y-6">
      {/* Top Banner & Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-white tracking-tight flex items-center gap-2.5">
            <span>{language === 'en' ? 'Fleet Telemetry Command Center' : 'ফ্লিট টেলিমেট্রি কমান্ড সেন্টার'}</span>
            <span className="px-2.5 py-0.5 rounded-full bg-monexa-cyan/20 border border-monexa-cyan/50 text-xs font-mono text-monexa-cyan">
              LIVE
            </span>
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            {language === 'en' 
              ? 'Real-time telemetry, live screen viewing, and pairing access control.'
              : 'রিয়েল-টাইম টেলিমেট্রি, লাইভ স্ক্রিন প্রিভিউ এবং পেয়ারিং অ্যাক্সেস কন্ট্রোল।'}
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={onRefresh}
            className="p-2.5 rounded-xl bg-monexa-surface hover:bg-monexa-surfaceLight border border-monexa-border text-slate-300 hover:text-white transition-colors"
            title="Refresh Fleet"
          >
            <RefreshCw className="w-4 h-4" />
          </button>

          <button
            onClick={() => setIsPairingModalOpen(true)}
            className="px-4 py-2.5 rounded-xl bg-monexa-cyan hover:bg-cyan-400 text-slate-950 font-bold text-sm flex items-center gap-2 shadow-lg shadow-monexa-cyan/20 transition-all"
          >
            <Plus className="w-4 h-4" />
            <span>{language === 'en' ? 'Add Device by 6-Digit ID' : '৬-সংখ্যার আইডি দিয়ে যোগ করুন'}</span>
          </button>
        </div>
      </div>

      {/* KPI Stats Grid */}
      <div className="grid grid-cols-2 md:grid-cols-5 gap-3.5">
        {/* Total Registered */}
        <div className="bg-monexa-surface border border-monexa-border rounded-xl p-4 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">{language === 'en' ? 'Total Devices' : 'মোট ডিভাইস'}</span>
            <Smartphone className="w-4 h-4 text-monexa-cyan" />
          </div>
          <p className="text-2xl font-mono font-extrabold text-white">{totalCount}</p>
        </div>

        {/* Connected / Online */}
        <div className="bg-monexa-surface border border-monexa-border rounded-xl p-4 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">{language === 'en' ? 'Connected' : 'সংযুক্ত'}</span>
            <Wifi className="w-4 h-4 text-monexa-emerald" />
          </div>
          <p className="text-2xl font-mono font-extrabold text-monexa-emerald">{onlineCount}</p>
        </div>

        {/* Offline */}
        <div className="bg-monexa-surface border border-monexa-border rounded-xl p-4 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">{language === 'en' ? 'Offline' : 'অফলাইন'}</span>
            <WifiOff className="w-4 h-4 text-slate-500" />
          </div>
          <p className="text-2xl font-mono font-extrabold text-slate-400">{offlineCount}</p>
        </div>

        {/* Pending Approval */}
        <div className="bg-monexa-surface border border-monexa-border rounded-xl p-4 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">{language === 'en' ? 'Pending Approval' : 'অনুমোদন বাকি'}</span>
            <Clock className="w-4 h-4 text-monexa-amber" />
          </div>
          <p className="text-2xl font-mono font-extrabold text-monexa-amber">{pendingCount}</p>
        </div>

        {/* Active Screen Sessions */}
        <div className="bg-monexa-surface border border-monexa-border rounded-xl p-4 flex flex-col justify-between col-span-2 md:col-span-1">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">{language === 'en' ? 'Live Screens' : 'স্ক্রিন সেশন'}</span>
            <Tv className="w-4 h-4 text-monexa-blue" />
          </div>
          <p className="text-2xl font-mono font-extrabold text-monexa-blue">{activeScreenSessions}</p>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="bg-monexa-surface border border-monexa-border rounded-2xl p-4 flex flex-col sm:flex-row items-center justify-between gap-3">
        <div className="relative w-full sm:w-80">
          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder={language === 'en' ? "Search by 6-digit ID, name..." : "আইডি বা নাম দিয়ে খুঁজুন..."}
            className="w-full bg-monexa-dark border border-monexa-border focus:border-monexa-cyan rounded-xl pl-10 pr-4 py-2 text-xs text-slate-200 placeholder:text-slate-500 focus:outline-none transition-colors font-mono"
          />
        </div>

        {/* Filter Pills */}
        <div className="flex items-center gap-1.5 w-full sm:w-auto overflow-x-auto pb-1 sm:pb-0">
          {['ALL', 'PAIRED', 'PENDING', 'ONLINE'].map((status) => (
            <button
              key={status}
              onClick={() => setFilterStatus(status)}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-colors whitespace-nowrap ${
                filterStatus === status
                  ? 'bg-monexa-cyan text-slate-950 shadow-md shadow-monexa-cyan/20'
                  : 'bg-monexa-surfaceLight hover:bg-monexa-border text-slate-300'
              }`}
            >
              {status === 'ALL' && (language === 'en' ? 'All Devices' : 'সব ডিভাইস')}
              {status === 'PAIRED' && (language === 'en' ? 'Paired' : 'সংযুক্ত')}
              {status === 'PENDING' && (language === 'en' ? 'Pending' : 'অপেক্ষমান')}
              {status === 'ONLINE' && (language === 'en' ? 'Online' : 'অনলাইন')}
            </button>
          ))}
        </div>
      </div>

      {/* Device Cards Grid */}
      {filteredDevices.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredDevices.map((device) => (
            <DeviceCard
              key={device.id || device.deviceId}
              device={device}
              language={language}
              onSelect={onSelectDevice}
            />
          ))}
        </div>
      ) : (
        <div className="bg-monexa-surface border border-monexa-border rounded-2xl p-12 text-center max-w-lg mx-auto space-y-4">
          <div className="w-14 h-14 rounded-2xl bg-monexa-cyan/10 border border-monexa-cyan/30 mx-auto flex items-center justify-center text-monexa-cyan">
            <Smartphone className="w-7 h-7" />
          </div>
          <h3 className="text-lg font-bold text-white">
            {language === 'en' ? 'No Registered Devices Found' : 'কোনো ডিভাইস পাওয়া যায়নি'}
          </h3>
          <p className="text-xs text-slate-400 leading-relaxed">
            {language === 'en'
              ? 'Open the MONEXA Customer App on your Android device to generate a unique 6-digit Device ID, then click "Add Device" to initiate pairing.'
              : 'আপনার অ্যান্ড্রয়েড ডিভাইসে মনেক্সা গ্রাহক অ্যাপটি খুলুন এবং ৬-সংখ্যার ডিভাইস আইডি তৈরি করুন। তারপর পেয়ারিং শুরু করতে "যোগ করুন" চাপুন।'}
          </p>
          <button
            onClick={() => setIsPairingModalOpen(true)}
            className="px-5 py-2.5 rounded-xl bg-monexa-cyan hover:bg-cyan-400 text-slate-950 font-bold text-xs inline-flex items-center gap-2 shadow-lg shadow-monexa-cyan/20 transition-all"
          >
            <Plus className="w-4 h-4" />
            <span>{language === 'en' ? 'Pair Customer Device' : 'কাস্টমার ডিভাইস পেয়ার করুন'}</span>
          </button>
        </div>
      )}

      {/* Pairing Modal */}
      <PairingModal
        isOpen={isPairingModalOpen}
        onClose={() => setIsPairingModalOpen(false)}
        language={language}
        onPairingSent={() => {
          onRefresh();
        }}
      />
    </div>
  );
};
