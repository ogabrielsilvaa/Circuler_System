import { View, Text, TouchableOpacity, StyleSheet } from 'react-native'
import { MapPin } from 'lucide-react-native'
import { CollectionPoint } from '../../../../types/collection-point.types'

const styles = StyleSheet.create({
  iconContainer: {
    width: 56,
    height: 56,
    borderRadius: 12,
    backgroundColor: '#10b981',
    alignItems: 'center',
    justifyContent: 'center',
  },
})

type CollectionPointCardProps = { point: CollectionPoint; onPress?: () => void }

export function CollectionPointCard({ point, onPress }: CollectionPointCardProps) {
  return (
    <TouchableOpacity activeOpacity={0.7} onPress={onPress}>
      <View className="bg-white rounded-2xl p-4 shadow flex-row gap-4 items-center">
        <View style={styles.iconContainer}>
          <MapPin size={24} color="#047857" />
        </View>

        <View className="flex-1 gap-1">
          <Text className="text-base font-bold text-black" numberOfLines={2}>
            {point.name}
          </Text>
          <Text className="text-sm text-gray-500" numberOfLines={1}>
            {point.addressStreet}
          </Text>
          <Text className="text-xs text-gray-400" numberOfLines={1}>
            {point.addressNeighborhood}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  )
}
