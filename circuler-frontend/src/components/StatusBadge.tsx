import { View, Text } from 'react-native'
import { BookInstanceStatus } from '../constants/enums'

type StatusBadgeProps = { status: BookInstanceStatus }

export function StatusBadge({ status }: StatusBadgeProps) {
  if (status === 'DISPONIVEL') {
    return (
      <View className="bg-emerald-100 rounded-full px-2 py-0.5">
        <Text className="text-xs font-medium text-emerald-800">Disponível</Text>
      </View>
    )
  }
  if (status === 'RESERVADO') {
    return (
      <View className="bg-amber-100 rounded-full px-2 py-0.5">
        <Text className="text-xs font-medium text-amber-800">Reservado</Text>
      </View>
    )
  }
  return (
    <View className="bg-gray-100 rounded-full px-2 py-0.5">
      <Text className="text-xs font-medium text-gray-600">Indisponível</Text>
    </View>
  )
}
