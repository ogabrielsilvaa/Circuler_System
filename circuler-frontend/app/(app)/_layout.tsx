import { Stack, Redirect } from 'expo-router';
import { View } from 'react-native';
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
      <View className="flex-1">
        <Stack screenOptions={{ headerShown: false }} />
      </View>
      <AppNavbar />
    </SafeAreaView>
  );
}
