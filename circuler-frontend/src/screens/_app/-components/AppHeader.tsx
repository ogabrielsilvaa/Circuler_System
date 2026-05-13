import { View, Text } from 'react-native'
import { LeafyGreen } from 'lucide-react-native'

export function AppHeader() {
  return (
    <View className="bg-emerald-600 px-4 py-3 flex-row items-center">
      <View className="bg-emerald-700 rounded-xl px-3 py-2 flex-row items-center gap-x-2">
        <LeafyGreen size={20} color="#f8fafc" />
        <Text className="text-white font-bold text-base">Circuler</Text>
      </View>
    </View>
  )
}
