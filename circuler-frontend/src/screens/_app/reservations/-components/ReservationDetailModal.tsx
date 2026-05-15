import { Modal, View, Text, StyleSheet } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen, MapPin } from 'lucide-react-native'
import { Button } from '../../../../components/Button'
import { ReservationResponse } from '../../../../types/reservation.types'

const styles = StyleSheet.create({
  thumbnailContainer: {
    width: 80,
    height: 112,
    borderRadius: 12,
    backgroundColor: '#047857',
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  thumbnail: { flex: 1, width: '100%' },
})

type Props = {
  visible: boolean
  reservation: ReservationResponse | null
  onClose: () => void
}

export function ReservationDetailModal({ visible, reservation, onClose }: Props) {
  if (!reservation) return null

  return (
    <Modal visible={visible} transparent animationType="fade">
      <View className="flex-1 items-center justify-center bg-black/60 px-6">
        <View className="w-full bg-emerald-800 rounded-2xl p-6 gap-5">
          <View className="items-center gap-3">
            <View style={styles.thumbnailContainer}>
              {reservation.bookThumbnailUrl ? (
                <Image
                  source={{ uri: reservation.bookThumbnailUrl }}
                  style={styles.thumbnail}
                  contentFit="cover"
                />
              ) : (
                <BookOpen size={36} color="#fff" />
              )}
            </View>
            <Text className="text-white font-bold text-lg text-center" numberOfLines={2}>
              {reservation.bookTitle}
            </Text>
          </View>

          <View className="items-center gap-2">
            <Text className="text-emerald-200 text-sm text-center">Código de verificação</Text>
            <View className="bg-gray-100 rounded-xl px-6 py-4 w-full items-center">
              <Text className="text-emerald-800 font-bold text-2xl text-center tracking-widest">
                {reservation.verificationCode}
              </Text>
            </View>
            <Text className="text-emerald-200 text-xs text-center">
              Apresente este código ao retirar o livro
            </Text>
          </View>

          <View className="flex-row items-start gap-2">
            <MapPin size={14} color="#6ee7b7" />
            <View className="flex-1 gap-0.5">
              <Text className="text-white font-bold text-sm">{reservation.collectionPointName}</Text>
              <Text className="text-emerald-200 text-xs">
                {reservation.collectionPointAddressStreet} - {reservation.collectionPointAddressNeighborhood}
              </Text>
            </View>
          </View>

          <Button label="Fechar" variant="secondary" onPress={onClose} />
        </View>
      </View>
    </Modal>
  )
}
