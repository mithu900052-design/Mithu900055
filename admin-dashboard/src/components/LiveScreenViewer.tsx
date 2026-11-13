import React, { useState, useRef, useEffect } from 'react';
import { 
  Tv, 
  ZoomIn, 
  ZoomOut, 
  Maximize2, 
  RotateCcw, 
  Play, 
  Pause, 
  ShieldCheck, 
  Move, 
  AlertCircle,
  Eye,
  Lock
} from 'lucide-react';
import { Device } from '../types/monexa';

interface LiveScreenViewerProps {
  device: Device;
  language: 'en' | 'bn';
}

export const LiveScreenViewer: React.FC<LiveScreenViewerProps> = ({ device, language }) => {
  const [zoom, setZoom] = useState<number>(1);
  const [pan, setPan] = useState<{ x: number; y: number }>({ x: 0, y: 0 });
  const [isPanning, setIsPanning] = useState<boolean>(false);
  const [startPan, setStartPan] = useState<{ x: number; y: number }>({ x: 0, y: 0 });
  const [isPaused, setIsPaused] = useState<boolean>(false);
  const [isFullscreen, setIsFullscreen] = useState<boolean>(false);
  const [sessionSeconds, setSessionSeconds] = useState<number>(0);

  const containerRef = useRef<HTMLDivElement>(null);

  // Session duration timer
  useEffect(() => {
    let interval: any = null;
    if (device.screenSharingEnabled && !isPaused) {
      interval = setInterval(() => {
        setSessionSeconds(prev => prev + 1);
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [device.screenSharingEnabled, isPaused]);

  // Independent canvas pan handlers (strictly client-side transform only!)
  const handleMouseDown = (e: React.MouseEvent) => {
    setIsPanning(true);
    setStartPan({ x: e.clientX - pan.x, y: e.clientY - pan.y });
  };

  const handleMouseMove = (e: React.MouseEvent) => {
    if (!isPanning) return;
    setPan({
      x: e.clientX - startPan.x,
      y: e.clientY - startPan.y,
    });
  };

  const handleMouseUp = () => {
    setIsPanning(false);
  };

  const handleZoomIn = () => setZoom(prev => Math.min(prev + 0.25, 3));
  const handleZoomOut = () => setZoom(prev => Math.max(prev - 0.25, 0.5));
  const handleResetView = () => {
    setZoom(1);
    setPan({ x: 0, y: 0 });
  };

  const toggleFullscreen = () => {
    if (!containerRef.current) return;
    if (!document.fullscreenElement) {
      containerRef.current.requestFullscreen().catch(() => {});
      setIsFullscreen(true);
    } else {
      document.exitFullscreen().catch(() => {});
      setIsFullscreen(false);
    }
  };

  const formatDuration = (sec: number) => {
    const mins = Math.floor(sec / 60);
    const s = sec % 60;
    return `${mins.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  return (
    <div 
      ref={containerRef}
      className={`bg-monexa-surface border border-monexa-border rounded-2xl flex flex-col overflow-hidden shadow-2xl transition-all ${
        isFullscreen ? 'fixed inset-0 z-50 rounded-none bg-monexa-dark' : 'h-[620px]'
      }`}
    >
      {/* Top Controller Bar */}
      <div className="bg-monexa-surfaceLight/80 border-b border-monexa-border px-5 py-3 flex items-center justify-between flex-wrap gap-3">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-lg bg-monexa-cyan/20 border border-monexa-cyan/50 flex items-center justify-center text-monexa-cyan">
            <Tv className="w-4 h-4" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-extrabold text-sm text-white tracking-wide">
                MONEXA LIVE SCREEN VIEWER
              </span>
              <span className="px-2 py-0.5 rounded-full bg-emerald-950 border border-emerald-800 text-[10px] font-semibold text-emerald-400">
                MIRACAST STREAM
              </span>
            </div>
            <p className="text-[11px] text-slate-400">
              {language === 'en' ? 'Touch-Isolated Independent Viewing Canvas' : 'টাচ-বিচ্ছিন্ন স্বাধীন ভিউয়ার ক্যানভাস'}
            </p>
          </div>
        </div>

        {/* Stream Actions & Controls */}
        <div className="flex items-center gap-2">
          {/* Touch-Isolation Guarantee Pill */}
          <div className="hidden lg:flex items-center gap-1.5 px-3 py-1 rounded-lg bg-monexa-dark/80 border border-monexa-border text-[11px] text-monexa-cyan font-mono">
            <Lock className="w-3.5 h-3.5" />
            <span>{language === 'en' ? 'Touch Isolation: Active' : 'টাচ আইসোলেশন: সক্রিয়'}</span>
          </div>

          {/* Pause / Resume */}
          <button
            onClick={() => setIsPaused(!isPaused)}
            className="p-2 rounded-lg bg-monexa-dark border border-monexa-border hover:border-monexa-cyan text-slate-300 hover:text-white transition-colors"
            title={isPaused ? "Resume" : "Pause"}
          >
            {isPaused ? <Play className="w-4 h-4 text-monexa-emerald" /> : <Pause className="w-4 h-4 text-monexa-amber" />}
          </button>

          {/* Zoom In */}
          <button
            onClick={handleZoomIn}
            className="p-2 rounded-lg bg-monexa-dark border border-monexa-border hover:border-monexa-cyan text-slate-300 hover:text-white transition-colors"
            title="Zoom In"
          >
            <ZoomIn className="w-4 h-4" />
          </button>

          {/* Zoom Out */}
          <button
            onClick={handleZoomOut}
            className="p-2 rounded-lg bg-monexa-dark border border-monexa-border hover:border-monexa-cyan text-slate-300 hover:text-white transition-colors"
            title="Zoom Out"
          >
            <ZoomOut className="w-4 h-4" />
          </button>

          {/* Reset View */}
          <button
            onClick={handleResetView}
            className="p-2 rounded-lg bg-monexa-dark border border-monexa-border hover:border-monexa-cyan text-slate-300 hover:text-white transition-colors"
            title="Reset Pan & Zoom"
          >
            <RotateCcw className="w-4 h-4" />
          </button>

          {/* Fullscreen */}
          <button
            onClick={toggleFullscreen}
            className="p-2 rounded-lg bg-monexa-dark border border-monexa-border hover:border-monexa-cyan text-slate-300 hover:text-white transition-colors"
            title="Toggle Fullscreen"
          >
            <Maximize2 className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Touch-Isolation Explicit Notice Bar */}
      <div className="bg-blue-950/40 border-b border-blue-900/40 px-4 py-2 flex items-center justify-between text-xs text-blue-300">
        <div className="flex items-center gap-2">
          <ShieldCheck className="w-4 h-4 text-monexa-cyan shrink-0" />
          <span>
            {language === 'en'
              ? 'Independent Viewer Scrolling: Panning or zooming this window never sends touch events to the phone.'
              : 'স্বাধীন স্ক্রোলিং: এই ভিউয়ার নাড়াচাড়া বা জুম করলে গ্রাহকের ফোনে কোনো স্পর্শ ইনপুট যাবে না।'}
          </span>
        </div>
        <div className="flex items-center gap-3 font-mono text-[11px]">
          <span>Zoom: {Math.round(zoom * 100)}%</span>
          <span>Duration: {formatDuration(sessionSeconds)}</span>
        </div>
      </div>

      {/* Main Interactive Screen Canvas with Independent Pan & Zoom */}
      <div 
        className="flex-1 bg-black relative overflow-hidden flex items-center justify-center cursor-grab active:cursor-grabbing select-none"
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp}
      >
        {/* Background Grid Pattern */}
        <div 
          className="absolute inset-0 opacity-10 pointer-events-none"
          style={{
            backgroundImage: 'radial-gradient(circle, #00E5FF 1px, transparent 1px)',
            backgroundSize: '24px 24px'
          }}
        />

        {/* Phone Frame Canvas Display */}
        <div 
          className="relative transition-transform duration-75"
          style={{
            transform: `translate(${pan.x}px, ${pan.y}px) scale(${zoom})`,
            transformOrigin: 'center center'
          }}
        >
          {/* Simulated Mobile Device Preview with Live Telemetry */}
          <div className="w-[300px] h-[580px] bg-[#0A0F1D] border-4 border-slate-700 rounded-[38px] p-3 shadow-2xl relative flex flex-col justify-between overflow-hidden">
            {/* Phone Notch & Speaker */}
            <div className="w-24 h-4 bg-slate-800 rounded-b-xl mx-auto mb-2 flex items-center justify-center">
              <div className="w-8 h-1 bg-slate-700 rounded-full"></div>
            </div>

            {/* Screen Content Stream */}
            <div className="flex-1 rounded-2xl bg-gradient-to-b from-[#10192E] to-[#0A0F1D] p-3 flex flex-col justify-between border border-monexa-border/60">
              <div>
                {/* Simulated Android Status Bar */}
                <div className="flex justify-between items-center text-[10px] text-slate-400 font-mono mb-4 px-1">
                  <span>10:45 AM</span>
                  <div className="flex items-center gap-1">
                    <span>5G</span>
                    <span>{device.batteryPercentage}%</span>
                  </div>
                </div>

                {/* App in Action */}
                <div className="space-y-3">
                  <div className="p-3 rounded-xl bg-monexa-cyan/10 border border-monexa-cyan/30 text-center">
                    <p className="text-[10px] font-mono uppercase tracking-wider text-monexa-cyan">
                      MONEXA MONITOR
                    </p>
                    <p className="text-xs font-bold text-white mt-1">
                      {device.customerName}'s Device
                    </p>
                    <p className="text-[11px] font-mono text-monexa-cyan mt-1">
                      ID: {device.deviceId}
                    </p>
                  </div>

                  <div className="p-2.5 rounded-lg bg-monexa-surfaceLight/50 border border-monexa-border text-[11px] space-y-1">
                    <div className="flex justify-between text-slate-400">
                      <span>Status:</span>
                      <span className="text-monexa-emerald font-semibold">MediaProjection Stream</span>
                    </div>
                    <div className="flex justify-between text-slate-400">
                      <span>Model:</span>
                      <span className="text-slate-200">{device.model || 'Android Phone'}</span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Watermark notice */}
              <div className="text-center py-2 bg-black/40 rounded-lg border border-monexa-border/30">
                <span className="text-[9px] font-mono text-slate-400">
                  MIRACAST SECURE FEED • NO REMOTE INJECTION
                </span>
              </div>
            </div>

            {/* Home indicator bar */}
            <div className="w-28 h-1 bg-slate-600 rounded-full mx-auto mt-2"></div>
          </div>
        </div>

        {/* Pan Helper Badge */}
        <div className="absolute bottom-3 left-3 bg-monexa-dark/80 backdrop-blur-md border border-monexa-border rounded-lg px-2.5 py-1.5 flex items-center gap-1.5 text-[11px] text-slate-400 pointer-events-none">
          <Move className="w-3.5 h-3.5 text-monexa-cyan" />
          <span>Click & Drag to Pan Preview</span>
        </div>
      </div>
    </div>
  );
};
