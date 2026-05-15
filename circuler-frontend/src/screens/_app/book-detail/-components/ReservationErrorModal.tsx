import { Modal, View, Text } from 'react-native'
import { XCircle } from 'lucide-react-native'
import { Button } from '../../../../components/Button'

type Props = {
  visible: boolean
  message: string
  onClose: () => void
}

export function ReservationErrorModal({ visible, message, onClose }: Props) {
  return (
    <Modal visible={visible} transparent animationType="fade">
      <View className="flex-1 items-center justify-center bg-black/60 px-6">
        <View className="w-full bg-emerald-800 rounded-2xl p-6 gap-5">
          <View className="items-center gap-2">
            <XCircle size={40} color="#fff" />
            <Text className="text-white font-bold text-xl text-center">
              Não foi possível realizar a reserva
            </Text>
          </View>

          <View className="bg-gray-100 rounded-xl px-6 py-4 w-full items-center">
            <Text className="text-emerald-800 text-sm text-center">{message}</Text>
          </View>

          <Button label="Entendido" variant="secondary" onPress={onClose} />
        </View>
      </View>
    </Modal>
  )
}
