import { useState, useEffect } from 'react'
import { ScrollView, View, ActivityIndicator } from 'react-native'
import { HeroCard } from './-components/HeroCard'
import { CategoryFilter } from './-components/CategoryFilter'
import { BookInstanceList } from './-components/BookInstanceList'
import { DonateModal } from './-components/DonateModal'
import { Button } from '../../../components/Button'
import { useBookInstances, useBookInstanceSearch, useBookInstancesByCategory } from '../../../hooks/useBookInstances'

export default function Home() {
  const [searchQuery, setSearchQuery] = useState('')
  const [debouncedQuery, setDebouncedQuery] = useState('')
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null)
  const [donateVisible, setDonateVisible] = useState(false)

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedQuery(searchQuery), 400)
    return () => clearTimeout(timer)
  }, [searchQuery])

  const { bookInstances: allInstances, isLoading: loadingAll } = useBookInstances()
  const { bookInstances: searchInstances, isLoading: loadingSearch } = useBookInstanceSearch(debouncedQuery)
  const { bookInstances: categoryInstances, isLoading: loadingCategory } = useBookInstancesByCategory(selectedCategory)

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
    <>
      <ScrollView className="flex-1 bg-gray-100" contentContainerStyle={{ paddingBottom: 24 }}>
        <View className="px-4 pt-4">
          <HeroCard searchQuery={searchQuery} onSearchChange={handleSearchChange} />
        </View>
        <View className="mt-4 px-4">
          <Button label="Doar um Livro" variant="secondary" onPress={() => setDonateVisible(true)} />
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
      <DonateModal visible={donateVisible} onClose={() => setDonateVisible(false)} />
    </>
  )
}
