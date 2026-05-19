import { Stack } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { useEffect } from 'react';
import { useFonts } from 'expo-font';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useAuthStore } from '../src/stores/auth.store';
import 'react-native-reanimated';
import '../src/styles/global.css';

export { ErrorBoundary } from 'expo-router';

SplashScreen.preventAutoHideAsync();

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        if (error instanceof AxiosError && error.response && error.response.status < 500) {
          return false;
        }
        return failureCount < 3;
      },
    },
  },
});

export default function RootLayout() {
  const hydrateFromStorage = useAuthStore((state) => state.hydrateFromStorage);
  const isHydrating = useAuthStore((state) => state.isHydrating);

  const [fontsLoaded, fontError] = useFonts({
    'DMSans-Regular': require('../src/assets/fonts/DMSans-Regular.ttf'),
    'DMSans-Medium': require('../src/assets/fonts/DMSans-Medium.ttf'),
    'DMSans-Bold': require('../src/assets/fonts/DMSans-Bold.ttf'),
  });

  useEffect(() => {
    hydrateFromStorage();
  }, [hydrateFromStorage]);

  useEffect(() => {
    if (fontError) throw fontError;
  }, [fontError]);

  useEffect(() => {
    if (fontsLoaded && !isHydrating) {
      SplashScreen.hideAsync();
    }
  }, [fontsLoaded, isHydrating]);

  if (!fontsLoaded && !fontError) return null;

  return (
    <QueryClientProvider client={queryClient}>
      <Stack screenOptions={{ headerShown: false }} />
    </QueryClientProvider>
  );
}
