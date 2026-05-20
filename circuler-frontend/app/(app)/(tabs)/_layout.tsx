import { Tabs } from 'expo-router';

export default function TabsLayout() {
  return (
    <Tabs tabBar={() => null} screenOptions={{ headerShown: false }}>
      <Tabs.Screen name="index" />
      <Tabs.Screen name="collection-points" />
      <Tabs.Screen name="reservations" />
      <Tabs.Screen name="profile" />
    </Tabs>
  );
}
