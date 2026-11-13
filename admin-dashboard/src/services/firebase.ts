import { initializeApp, getApps } from 'firebase/app';
import { 
  getFirestore, 
  collection, 
  doc, 
  onSnapshot, 
  setDoc, 
  updateDoc, 
  serverTimestamp, 
  query, 
  where,
  addDoc 
} from 'firebase/firestore';
import { Device, PairingRequest } from '../types/monexa';

// Firebase Project & Database configuration from ProvisionFirebase
const firebaseConfig = {
  projectId: "gen-lang-client-0977118472",
  appId: "1:279391564935:web:monexa-admin",
  databaseURL: "https://gen-lang-client-0977118472.firebaseio.com"
};

const app = getApps().length === 0 ? initializeApp(firebaseConfig) : getApps()[0];

// Custom Firestore database instance
export const db = getFirestore(app, "ai-studio-android-monexamo-5938685a-a859-490b-902c-2090e00ca09f");

/**
 * Real-time listener for all registered devices
 */
export function subscribeToDevices(onUpdate: (devices: Device[]) => void, onError?: (err: any) => void) {
  const devicesCol = collection(db, 'devices');
  return onSnapshot(devicesCol, (snapshot) => {
    const devices: Device[] = [];
    snapshot.forEach((d) => {
      devices.push({ id: d.id, ...d.data() } as Device);
    });
    onUpdate(devices);
  }, (err) => {
    console.warn("Firestore snapshot notice:", err);
    if (onError) onError(err);
  });
}

/**
 * Dispatches an admin pairing request for a 6-digit Device ID
 */
export async function sendPairingRequest(deviceId: string, adminId: string = 'admin_desk_01', adminName: string = 'Monexa Central Operations'): Promise<string> {
  const pairingCol = collection(db, 'device_pairings');
  const docRef = await addDoc(pairingCol, {
    deviceId: deviceId.trim(),
    adminId,
    adminName,
    status: 'PENDING',
    requestedAt: serverTimestamp(),
  });

  // Also update pairing status flag on device if document exists
  try {
    const deviceRef = doc(db, 'devices', deviceId.trim());
    await updateDoc(deviceRef, {
      pairingStatus: 'PENDING_APPROVAL',
      updatedAt: serverTimestamp(),
    });
  } catch (e) {
    // Non-blocking if device record is created on customer confirmation
  }

  return docRef.id;
}

/**
 * Subscribes to pending pairing requests
 */
export function subscribeToPairingRequests(onUpdate: (requests: PairingRequest[]) => void) {
  const q = query(collection(db, 'device_pairings'), where('status', '==', 'PENDING'));
  return onSnapshot(q, (snapshot) => {
    const reqs: PairingRequest[] = [];
    snapshot.forEach((d) => {
      reqs.push({ id: d.id, ...d.data() } as PairingRequest);
    });
    onUpdate(reqs);
  });
}

/**
 * Revokes pairing access from admin side
 */
export async function revokeDeviceAccess(deviceId: string): Promise<void> {
  const deviceRef = doc(db, 'devices', deviceId);
  await updateDoc(deviceRef, {
    pairingStatus: 'REVOKED',
    approvedAdminId: null,
    updatedAt: serverTimestamp(),
  });
}
