import { Tabs, Redirect } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useAuthStore } from '../../src/stores/auth.store';
import { AppHeader } from '../../src/screens/_app/-components/AppHeader';
import { AppNavbar } from '../../src/screens/_app/-components/AppNavbar';

export default function AppLayout() {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const isHydrating = useAuthStore((state) => state.isHydrating);

  if (isHydrating) return null;

  if (!isAuthenticated) {
    return <Redirect href="/(auth)/login" />;
  }

  return (
    <SafeAreaView className="flex-1 bg-emerald-600" edges={['top']}>
      <AppHeader />
      <Tabs
        tabBar={(props) => <AppNavbar {...props} />}
        screenOptions={{ headerShown: false }}
      >
        <Tabs.Screen name="index" />
        <Tabs.Screen name="search" />
        <Tabs.Screen name="reservations" />
        <Tabs.Screen name="profile" />
        <Tabs.Screen name="books/[id]" options={{ href: null }} />
      </Tabs>
    </SafeAreaView>
  );
}
