import { View, ScrollView } from 'react-native'
import { useAuthStore } from '../../../stores/auth.store'
import { Button } from '../../../components/Button'

export default function Profile() {
  const clearSession = useAuthStore(s => s.clearSession)

  return (
    <ScrollView className="flex-1 bg-gray-100" contentContainerStyle={{ padding: 24, flexGrow: 1 }}>
      <View className="flex-1 justify-end">
        <Button label="Sair" variant="secondary" onPress={clearSession} />
      </View>
    </ScrollView>
  )
}
