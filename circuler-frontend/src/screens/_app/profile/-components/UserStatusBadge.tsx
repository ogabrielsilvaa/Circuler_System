import { View, Text } from 'react-native'
import { UserStatus } from '../../../../types/user.types'

type Props = { status: UserStatus }

export function UserStatusBadge({ status }: Props) {
  if (status === 'ATIVO') {
    return (
      <View className="bg-emerald-100 rounded-full px-3 py-0.5 self-start">
        <Text className="text-xs font-medium text-emerald-800">Ativo</Text>
      </View>
    )
  }
  if (status === 'INATIVO') {
    return (
      <View className="bg-amber-100 rounded-full px-3 py-0.5 self-start">
        <Text className="text-xs font-medium text-amber-800">Inativo</Text>
      </View>
    )
  }
  return (
    <View className="bg-gray-100 rounded-full px-3 py-0.5 self-start">
      <Text className="text-xs font-medium text-gray-600">Removido</Text>
    </View>
  )
}
