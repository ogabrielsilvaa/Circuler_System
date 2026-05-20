import { View, Text, TouchableOpacity } from 'react-native'
import { useSafeAreaInsets } from 'react-native-safe-area-context'
import { usePathname, useRouter } from 'expo-router'
import { House, MapPin, BookMarked, User } from 'lucide-react-native'

type TabConfig = {
  name: string
  label: string
  route: string
  Icon: React.ComponentType<{ size: number; color: string }>
}

const TABS: TabConfig[] = [
  { name: 'index',             label: 'Início',   route: '/',                  Icon: House      },
  { name: 'collection-points', label: 'Pontos',   route: '/collection-points', Icon: MapPin     },
  { name: 'reservations',      label: 'Reservas', route: '/reservations',      Icon: BookMarked },
  { name: 'profile',           label: 'Perfil',   route: '/profile',           Icon: User       },
]

function getActiveTab(pathname: string): string {
  if (pathname.startsWith('/collection-points')) return 'collection-points'
  if (pathname.startsWith('/reservations')) return 'reservations'
  if (pathname.startsWith('/profile')) return 'profile'
  return 'index'
}

export function AppNavbar() {
  const insets = useSafeAreaInsets()
  const pathname = usePathname()
  const router = useRouter()
  const activeTab = getActiveTab(pathname)

  return (
    <View className="bg-white border-t border-gray-200 flex-row" style={{ paddingBottom: insets.bottom }}>
      {TABS.map((tab) => {
        const isFocused = activeTab === tab.name
        const color = isFocused ? '#059669' : '#9ca3af'

        return (
          <TouchableOpacity
            key={tab.name}
            className="flex-1 items-center py-3 gap-y-1"
            activeOpacity={0.7}
            onPress={() => router.navigate(tab.route as any)}
          >
            <tab.Icon size={22} color={color} />
            <Text className={`text-xs ${isFocused ? 'text-emerald-600 font-medium' : 'text-gray-400'}`}>
              {tab.label}
            </Text>
          </TouchableOpacity>
        )
      })}
    </View>
  )
}
