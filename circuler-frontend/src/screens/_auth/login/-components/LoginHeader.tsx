import { View, Text } from 'react-native';
import { LeafyGreen } from 'lucide-react-native';

export function LoginHeader() {
  return (
    <View className="items-center mb-8">
      <View className="bg-emerald-700 rounded-2xl px-8 py-6 flex-row items-center gap-x-3 shadow-lg shadow-black/40">
        <LeafyGreen size={32} color="#f8fafc" />
        <Text className="text-white font-bold text-2xl tracking-wide">
          Circuler
        </Text>
      </View>
    </View>
  );
}
