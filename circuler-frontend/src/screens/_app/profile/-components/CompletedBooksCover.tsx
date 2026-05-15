import { View, Text, ScrollView, StyleSheet } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen } from 'lucide-react-native'
import { ReservationResponse } from '../../../../types/reservation.types'

const styles = StyleSheet.create({
  cover: {
    width: 80,
    height: 112,
    borderRadius: 8,
    backgroundColor: '#10b981',
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
  },
  coverImage: { flex: 1, width: '100%' },
})

type Props = { reservations: ReservationResponse[] }

export function CompletedBooksCover({ reservations }: Props) {
  if (reservations.length === 0) {
    return (
      <Text className="text-sm text-gray-400">Nenhum livro retirado ainda.</Text>
    )
  }

  return (
    <ScrollView horizontal showsHorizontalScrollIndicator={false} className="gap-3">
      <View className="flex-row gap-3">
        {reservations.map((reservation) => (
          <View key={reservation.id} style={styles.cover}>
            {reservation.bookThumbnailUrl ? (
              <Image
                source={{ uri: reservation.bookThumbnailUrl }}
                style={styles.coverImage}
                contentFit="cover"
              />
            ) : (
              <BookOpen size={28} color="#047857" />
            )}
          </View>
        ))}
      </View>
    </ScrollView>
  )
}
