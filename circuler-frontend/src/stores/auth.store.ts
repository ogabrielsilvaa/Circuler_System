import { create } from 'zustand';
import * as SecureStore from 'expo-secure-store';
import { Platform } from 'react-native';
import { setAuthToken, setOnUnauthorized } from '../services/api';
import { AuthUser } from '../types/auth.types';

const TOKEN_KEY = 'circuler_auth_token';

async function saveToStorage(key: string, value: string): Promise<void> {
  if (Platform.OS === 'web') {
    localStorage.setItem(key, value);
    return;
  }
  await SecureStore.setItemAsync(key, value);
}

async function loadFromStorage(key: string): Promise<string | null> {
  if (Platform.OS === 'web') {
    return localStorage.getItem(key);
  }
  return SecureStore.getItemAsync(key);
}

async function removeFromStorage(key: string): Promise<void> {
  if (Platform.OS === 'web') {
    localStorage.removeItem(key);
    return;
  }
  await SecureStore.deleteItemAsync(key);
}

function registerUnauthorizedCallback(set: (state: Partial<AuthState>) => void): void {
  setOnUnauthorized(() => {
    removeFromStorage(TOKEN_KEY);
    setAuthToken(null);
    setOnUnauthorized(null);
    set({ token: null, user: null, isAuthenticated: false });
  });
}

type AuthState = {
  token: string | null;
  user: AuthUser | null;
  isAuthenticated: boolean;
  isHydrating: boolean;
  setSession: (token: string, user: AuthUser) => Promise<void>;
  clearSession: () => Promise<void>;
  hydrateFromStorage: () => Promise<void>;
};

export const useAuthStore = create<AuthState>((set) => ({
  token: null,
  user: null,
  isAuthenticated: false,
  isHydrating: true,

  setSession: async (token, user) => {
    await saveToStorage(TOKEN_KEY, JSON.stringify({ token, user }));
    setAuthToken(token);
    set({ token, user, isAuthenticated: true });
    registerUnauthorizedCallback(set);
  },

  clearSession: async () => {
    await removeFromStorage(TOKEN_KEY);
    setAuthToken(null);
    setOnUnauthorized(null);
    set({ token: null, user: null, isAuthenticated: false });
  },

  hydrateFromStorage: async () => {
    try {
      const raw = await loadFromStorage(TOKEN_KEY);
      if (raw) {
        const { token, user } = JSON.parse(raw) as { token: string; user: AuthUser };
        setAuthToken(token);
        set({ token, user, isAuthenticated: true });
        registerUnauthorizedCallback(set);
      }
    } catch {
      // corrupted storage — start fresh
    } finally {
      set({ isHydrating: false });
    }
  },
}));
