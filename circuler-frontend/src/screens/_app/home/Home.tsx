import { useState, useEffect } from 'react'
import { ScrollView, View, ActivityIndicator, TouchableOpacity, Text } from 'react-native'
import { SafeAreaView } from 'react-native-safe-area-context'
import { HeroCard } from './-components/HeroCard'
import { CategoryFilter } from './-components/CategoryFilter'
import { BookInstanceList } from './-components/BookInstanceList'
import { useBookInstances, useBookInstanceSearch, useBookInstancesByCategory } from '../../../hooks/useBookInstances'
import { useAuthStore } from '../../../stores/auth.store'

export default function Home() {
  const [searchQuery, setSearchQuery] = useState('')
  const [debouncedQuery, setDebouncedQuery] = useState('')
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null)
  const clearSession = useAuthStore(s => s.clearSession)

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedQuery(searchQuery), 400)
    return () => clearTimeout(timer)
  }, [searchQuery])

  const { data: allInstances = [], isLoading: loadingAll } = useBookInstances()
  const { data: searchInstances = [], isLoading: loadingSearch } = useBookInstanceSearch(debouncedQuery)
  const { data: categoryInstances = [], isLoading: loadingCategory } = useBookInstancesByCategory(selectedCategory)

  const isSearchActive = debouncedQuery.trim().length > 0
  const instances = isSearchActive ? searchInstances
    : selectedCategory !== null ? categoryInstances
    : allInstances
  const isLoading = isSearchActive ? loadingSearch
    : selectedCategory !== null ? loadingCategory
    : loadingAll

  function handleSearchChange(text: string) {
    setSearchQuery(text)
    setSelectedCategory(null)
  }

  function handleCategorySelect(code: number | null) {
    setSelectedCategory(code)
    setSearchQuery('')
    setDebouncedQuery('')
  }

  return (
    <SafeAreaView className="flex-1 bg-gray-100">
      <ScrollView contentContainerStyle={{ paddingBottom: 24 }}>
        <View className="px-4">
          <View className="items-end mb-2">
            <TouchableOpacity onPress={clearSession}>
              <Text className="text-red-500 text-sm font-medium">Sair</Text>
            </TouchableOpacity>
          </View>
          <HeroCard searchQuery={searchQuery} onSearchChange={handleSearchChange} />
        </View>
        <View className="mt-6 px-4">
          <CategoryFilter
            selected={selectedCategory}
            onSelect={handleCategorySelect}
          />
        </View>
        <View className="mt-6 px-4">
          {isLoading ? (
            <ActivityIndicator size="large" color="#059669" />
          ) : (
            <BookInstanceList instances={instances} />
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  )
}
