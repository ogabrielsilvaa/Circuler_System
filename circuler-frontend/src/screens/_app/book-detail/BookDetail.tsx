import { useState } from 'react'
import { ScrollView, View, Text, StyleSheet, ActivityIndicator, TouchableOpacity } from 'react-native'
import { Image } from 'expo-image'
import { ArrowLeft, BookOpen, MapPin } from 'lucide-react-native'
import { useLocalSearchParams, useRouter } from 'expo-router'
import { BOOK_CATEGORIES } from '../../../types/book.types'
import { BookInstanceStatus } from '../../../constants/enums'
import { useBookInstanceById } from '../../../hooks/useBookInstances'
import { useCreateReservation } from '../../../hooks/useReservations'
import { Button } from '../../../components/Button'
import { ReservationSuccessModal } from './-components/ReservationSuccessModal'
import { ReservationErrorModal } from './-components/ReservationErrorModal'

const styles = StyleSheet.create({
  coverContainer: {
    width: '100%',
    height: 280,
    backgroundColor: '#10b981',
    alignItems: 'center',
    justifyContent: 'center',
  },
  cover: { flex: 1, width: '100%' },
})

export default function BookDetail() {
  const { id } = useLocalSearchParams<{ id: string }>()
  const router = useRouter()
  const { bookInstance: instance, isLoading, isError } = useBookInstanceById(Number(id))
  const { createReservation, isPending } = useCreateReservation()
  const [reservationCode, setReservationCode] = useState<string | null>(null)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color="#059669" />
      </View>
    )
  }

  if (isError || !instance) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-4">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar o exemplar.
        </Text>
      </View>
    )
  }

  const categoryLabel =
    BOOK_CATEGORIES.find((c) => c.key === instance.bookCategory)?.label ?? instance.bookCategory

  function handleReservar() {
    createReservation(
      { bookInstanceId: instance!.id },
      {
        onSuccess: (reservation) => setReservationCode(reservation.verificationCode),
        onError: (error: unknown) => {
          const message =
            (error as { response?: { data?: { message?: string } } })?.response?.data?.message ??
            'Não foi possível realizar a reserva. Tente novamente.'
          setErrorMessage(message)
        },
      },
    )
  }

  const isDisponivel = instance.status === BookInstanceStatus.DISPONIVEL

  return (
    <View className="flex-1 bg-gray-100">
      <TouchableOpacity
        onPress={() => router.back()}
        className="absolute top-12 left-4 z-10 bg-black/30 rounded-full p-2"
        hitSlop={8}
      >
        <ArrowLeft size={20} color="#fff" />
      </TouchableOpacity>

      <ScrollView className="flex-1" contentContainerStyle={{ paddingBottom: 16 }}>
        <View style={styles.coverContainer}>
          {instance.bookThumbnailUrl ? (
            <Image source={{ uri: instance.bookThumbnailUrl }} style={styles.cover} contentFit="cover" />
          ) : (
            <BookOpen size={64} color="#047857" />
          )}
        </View>

        <View className="px-4 py-5 gap-3">
          <View className="bg-emerald-500 self-start rounded-full px-3 py-1">
            <Text className="text-xs text-emerald-800">{categoryLabel}</Text>
          </View>

          <Text className="text-xl font-bold text-black">{instance.bookTitle}</Text>

          <Text className="text-sm text-gray-500">{instance.bookAuthor}</Text>

          <View className="h-px bg-gray-200 my-4" />

          <View>
            <View className="flex-row items-center gap-2">
              <MapPin size={16} color="#059669" />
              <Text className="text-base font-bold text-black">Estoque Físico Disponível</Text>
            </View>

            <View className="bg-white rounded-2xl p-4 shadow mt-3">
              <Text className="text-base font-bold text-black">{instance.collectionPointName}</Text>
              <Text className="text-sm text-gray-500 mt-1">
                {instance.collectionPointAddressStreet} - {instance.collectionPointAddressNeighborhood}
              </Text>
              <Text className="text-sm text-gray-400 mt-0.5">{instance.collectionPointOwnerName}</Text>
            </View>
          </View>
        </View>
      </ScrollView>

      <ReservationErrorModal
        visible={errorMessage !== null}
        message={errorMessage ?? ''}
        onClose={() => setErrorMessage(null)}
      />

      <ReservationSuccessModal
        visible={reservationCode !== null}
        verificationCode={reservationCode ?? ''}
        collectionPointName={instance.collectionPointName}
        collectionPointAddress={`${instance.collectionPointAddressStreet} - ${instance.collectionPointAddressNeighborhood}`}
        onClose={() => setReservationCode(null)}
      />

      <View className="px-4 pb-8 pt-3 bg-gray-100">
        <Button
          label={isPending ? 'Reservando...' : isDisponivel ? 'Reservar Exemplar' : 'Exemplar Indisponível'}
          variant="primary"
          className={isDisponivel && !isPending ? 'bg-emerald-800' : 'bg-gray-400'}
          onPress={handleReservar}
          disabled={isPending || !isDisponivel}
        />
      </View>
    </View>
  )
}
