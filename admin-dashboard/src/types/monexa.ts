export type PairingStatus = 'UNPAIRED' | 'PENDING_APPROVAL' | 'PAIRED' | 'REVOKED';

export interface Device {
  id: string; // Document ID (usually matches deviceId)
  deviceId: string; // Exactly 6 numerical digits
  userId: string;
  customerName: string;
  customerAge?: number;
  pairingStatus: PairingStatus;
  approvedAdminId?: string | null;
  model: string;
  manufacturer: string;
  androidVersion: string;
  appVersion: string;
  batteryPercentage: number;
  isCharging: boolean;
  availableStorageGb: number;
  totalStorageGb: number;
  networkType: string;
  isOnline: boolean;
  locationSharingEnabled: boolean;
  screenSharingEnabled: boolean;
  usageAccessGranted: boolean;
  lastSyncTime?: any;
  createdAt?: any;
  updatedAt?: any;
}

export interface PairingRequest {
  id: string;
  deviceId: string;
  adminId: string;
  adminName: string;
  userId: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'REVOKED';
  requestedAt?: any;
  respondedAt?: any;
}

export interface LocationRecord {
  id: string;
  deviceId: string;
  userId: string;
  latitude: number;
  longitude: number;
  accuracy: number;
  timestamp: any;
}

export interface AppUsageRecord {
  id: string;
  deviceId: string;
  packageName: string;
  appName: string;
  usageTimeMinutes: number;
  lastTimeUsed: any;
}

export interface ScreenSession {
  id: string;
  deviceId: string;
  status: 'ACTIVE' | 'PAUSED' | 'STOPPED';
  startedAt: any;
  endedAt?: any;
}

export interface AuditLog {
  id: string;
  action: string;
  actorId: string;
  targetDeviceId: string;
  details: string;
  timestamp: any;
}

export interface AppRelease {
  id: string;
  versionCode: number;
  versionName: string;
  releaseNotes: string;
  isRequired: boolean;
  downloadUrl: string;
  releasedAt: any;
}
