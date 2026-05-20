import { useState } from 'react'
import { View, Text, FlatList, ActivityIndicator } from 'react-native'
import { ReservationResponse } from '../../../types/reservation.types'
import { useMyReservations } from '../../../hooks/useReservations'
import { ReservationCard } from './-components/ReservationCard'
import { ReservationDetailModal } from './-components/ReservationDetailModal'

export default function Reservations() {
  const [selectedReservation, setSelectedReservation] = useState<ReservationResponse | null>(null)
  const { reservations, isLoading, isError } = useMyReservations()

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color="#059669" />
      </View>
    )
  }

  if (isError) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-6">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar suas reservas.
        </Text>
      </View>
    )
  }

  return (
    <View className="flex-1 bg-gray-100">
      {reservations.length === 0 ? (
        <View className="flex-1 items-center justify-center px-6">
          <Text className="text-base text-gray-500 text-center">
            Você ainda não possui reservas.
          </Text>
        </View>
      ) : (
        <FlatList
          data={reservations}
          keyExtractor={(item) => String(item.id)}
          renderItem={({ item }) => (
            <ReservationCard
              reservation={item}
              onPress={() => setSelectedReservation(item)}
            />
          )}
          ItemSeparatorComponent={() => <View className="h-3" />}
          contentContainerStyle={{ padding: 16 }}
        />
      )}

      <ReservationDetailModal
        visible={selectedReservation !== null}
        reservation={selectedReservation}
        onClose={() => setSelectedReservation(null)}
      />
    </View>
  )
}
