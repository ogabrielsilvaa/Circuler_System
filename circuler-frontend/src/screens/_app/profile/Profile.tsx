import { View, Text, ScrollView } from 'react-native'
import { User } from 'lucide-react-native'
import { useAuthStore } from '../../../stores/auth.store'
import { Button } from '../../../components/Button'
import { ProfileInfoRow } from './-components/ProfileInfoRow'
import { UserResponse, UserStatus } from '../../../types/user.types'
import { colors } from '../../../styles/colors'

const MOCK_USER: UserResponse = {
  id: 1,
  name: 'Gabriel Silva',
  email: 'gabriel@email.com',
  cpf: '12345678900',
  status: 'ATIVO',
  roles: ['ROLE_USER'],
}

function formatCpf(cpf: string): string {
  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
}

function formatRole(roles: string[]): string {
  if (roles.includes('ROLE_ROOT_ADMIN')) return 'Administrador Raiz'
  if (roles.includes('ROLE_ADMIN')) return 'Administrador'
  return 'Usuário'
}

function UserStatusBadge({ status }: { status: UserStatus }) {
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

export default function Profile() {
  const clearSession = useAuthStore(s => s.clearSession)
  const user = MOCK_USER

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

      <Button label="Sair" variant="secondary" onPress={clearSession} />
    </ScrollView>
  )
}
