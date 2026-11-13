import React, { useState, useEffect } from 'react';
import { Device, PairingRequest } from './types/monexa';
import { subscribeToDevices, subscribeToPairingRequests } from './services/firebase';
import { Navbar } from './components/Navbar';
import { DashboardPage } from './pages/DashboardPage';
import { DeviceDetailPage } from './pages/DeviceDetailPage';

export const App: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [pairingRequests, setPairingRequests] = useState<PairingRequest[]>([]);
  const [selectedDevice, setSelectedDevice] = useState<Device | null>(null);
  const [language, setLanguage] = useState<'en' | 'bn'>('en');

  useEffect(() => {
    // Real-time Firestore sync for all devices
    const unsubscribeDevices = subscribeToDevices((updatedDevices) => {
      setDevices(updatedDevices);
      // Keep selected device synced if open
      if (selectedDevice) {
        const found = updatedDevices.find(d => d.deviceId === selectedDevice.deviceId);
        if (found) setSelectedDevice(found);
      }
    });

    // Real-time Firestore sync for pairing requests
    const unsubscribeRequests = subscribeToPairingRequests((requests) => {
      setPairingRequests(requests);
    });

    return () => {
      unsubscribeDevices();
      unsubscribeRequests();
    };
  }, [selectedDevice]);

  const onlineCount = devices.filter(d => d.isOnline).length;

  return (
    <div className="min-h-screen bg-monexa-dark flex flex-col text-slate-100">
      <Navbar
        language={language}
        onLanguageToggle={() => setLanguage(l => l === 'en' ? 'bn' : 'en')}
        onlineCount={onlineCount}
      />

      <main className="flex-1">
        {selectedDevice ? (
          <DeviceDetailPage
            device={selectedDevice}
            language={language}
            onBack={() => setSelectedDevice(null)}
            onRefresh={() => {}}
          />
        ) : (
          <DashboardPage
            devices={devices}
            pairingRequests={pairingRequests}
            language={language}
            onSelectDevice={(device) => setSelectedDevice(device)}
            onRefresh={() => {}}
          />
        )}
      </main>

      <footer className="border-t border-monexa-border py-4 px-6 text-center text-xs text-slate-500 font-mono">
        MONEXA MONITOR PLATFORM • PHASE 1 FOUNDATION ACTIVE • ZERO-TRUST ARCHITECTURE
      </footer>
    </div>
  );
};

export default App;
