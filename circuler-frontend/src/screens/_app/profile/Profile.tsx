import { View, Text, ScrollView, ActivityIndicator } from 'react-native'
import { User } from 'lucide-react-native'
import { useAuthStore } from '../../../stores/auth.store'
import { Button } from '../../../components/Button'
import { ProfileInfoRow } from './-components/ProfileInfoRow'
import { UserStatusBadge } from './-components/UserStatusBadge'
import { CompletedBooksCover } from './-components/CompletedBooksCover'
import { useUser } from '../../../hooks/useUser'
import { useCompletedReservations } from '../../../hooks/useReservations'
import { formatCpf, formatRole } from '../../../utils/validators'
import { colors } from '../../../styles/colors'

export default function Profile() {
  const clearSession = useAuthStore(s => s.clearSession)
  const userId = useAuthStore(s => s.user?.id ?? 0)

  const { data: user, isLoading, isError } = useUser(userId)
  const { data: completedReservations = [], isLoading: isLoadingReservations } = useCompletedReservations()

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color={colors.emerald[600]} />
      </View>
    )
  }

  if (isError || !user) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-6">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar o perfil.
        </Text>
      </View>
    )
  }

  return (
    <ScrollView className="flex-1 bg-gray-100" contentContainerStyle={{ padding: 24, paddingBottom: 40 }}>
      <View className="items-center mb-8 mt-4">
        <View className="w-24 h-24 rounded-full bg-emerald-600 items-center justify-center mb-4">
          <User size={48} color={colors.white} strokeWidth={1.5} />
        </View>
        <Text className="text-xl font-bold text-gray-800">{user.name}</Text>
      </View>

      <View className="bg-white rounded-2xl px-4 mb-6" style={{ shadowColor: '#000', shadowOpacity: 0.05, shadowRadius: 8, elevation: 2 }}>
        <ProfileInfoRow label="E-mail" value={user.email} />
        <ProfileInfoRow label="CPF" value={formatCpf(user.cpf)} />
        <ProfileInfoRow label="Perfil" value={formatRole(user.roles)} />
        <View className="py-4">
          <Text className="text-xs text-gray-400 mb-2 font-medium">Status</Text>
          <UserStatusBadge status={user.status} />
        </View>
      </View>

      <View className="mb-6">
        <Text className="text-base font-bold text-gray-800 mb-3">Livros Retirados</Text>
        {isLoadingReservations ? (
          <ActivityIndicator size="small" color={colors.emerald[600]} />
        ) : (
          <CompletedBooksCover reservations={completedReservations} />
        )}
      </View>

      <Button label="Sair" variant="secondary" onPress={clearSession} />
    </ScrollView>
  )
}
