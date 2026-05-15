import { View, Text, StyleSheet, TouchableOpacity } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen, MapPin } from 'lucide-react-native'
import { ReservationResponse } from '../../../../types/reservation.types'

const styles = StyleSheet.create({
  thumbnailContainer: {
    width: 80,
    height: 112,
    borderRadius: 12,
    backgroundColor: '#10b981',
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
  },
  thumbnail: { flex: 1, width: '100%' },
})

type ReservationCardProps = { reservation: ReservationResponse; onPress: () => void }

export function ReservationCard({ reservation, onPress }: ReservationCardProps) {
  return (
    <TouchableOpacity activeOpacity={0.7} onPress={onPress}>
      <View className="bg-white rounded-2xl p-4 shadow flex-row gap-4">
        <View style={styles.thumbnailContainer}>
          {reservation.bookThumbnailUrl ? (
            <Image
              source={{ uri: reservation.bookThumbnailUrl }}
              style={styles.thumbnail}
              contentFit="cover"
            />
          ) : (
            <BookOpen size={32} color="#047857" />
          )}
        </View>

        <View className="flex-1 gap-1">
          <Text className="text-base font-bold text-black" numberOfLines={2}>
            {reservation.bookTitle}
          </Text>

          <View className="flex-row items-center gap-1 mt-1">
            <MapPin size={12} color="#059669" />
            <Text className="text-xs text-gray-400 flex-1" numberOfLines={1}>
              {reservation.collectionPointName}
            </Text>
          </View>

          <Text className="text-xs text-gray-400" numberOfLines={1}>
            {reservation.collectionPointAddressStreet} - {reservation.collectionPointAddressNeighborhood}
          </Text>

          <View className="self-start bg-emerald-50 rounded-full px-2 py-0.5 mt-1">
            <Text className="text-xs text-emerald-800 font-medium">
              {reservation.verificationCode}
            </Text>
          </View>
        </View>
      </View>
    </TouchableOpacity>
  )
}
