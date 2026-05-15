import { Modal, View, Text } from 'react-native'
import { CheckCircle2 } from 'lucide-react-native'
import { Button } from '../../../../components/Button'

type Props = {
  visible: boolean
  verificationCode: string
  collectionPointName: string
  collectionPointAddress: string
  onClose: () => void
}

export function ReservationSuccessModal({
  visible,
  verificationCode,
  collectionPointName,
  collectionPointAddress,
  onClose,
}: Props) {
  return (
    <Modal visible={visible} transparent animationType="fade">
      <View className="flex-1 items-center justify-center bg-black/60 px-6">
        <View className="w-full bg-emerald-800 rounded-2xl p-6 gap-5">
          <View className="items-center gap-2">
            <CheckCircle2 size={40} color="#fff" />
            <Text className="text-white font-bold text-xl text-center">Reserva realizada!</Text>
          </View>

          <View className="items-center gap-2">
            <Text className="text-emerald-200 text-sm text-center">Código de verificação</Text>
            <View className="bg-gray-100 rounded-xl px-6 py-4 w-full items-center">
              <Text className="text-emerald-800 font-bold text-2xl text-center tracking-widest">
                {verificationCode}
              </Text>
            </View>
            <Text className="text-emerald-200 text-xs text-center">
              Apresente este código ao retirar o livro
            </Text>
          </View>

          <View className="gap-1">
            <Text className="text-white font-bold text-sm">{collectionPointName}</Text>
            <Text className="text-emerald-200 text-xs">{collectionPointAddress}</Text>
          </View>

          <Button label="Entendido" variant="secondary" onPress={onClose} />
        </View>
      </View>
    </Modal>
  )
}
