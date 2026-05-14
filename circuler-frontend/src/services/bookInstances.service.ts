import { api } from './api'
import { BookInstance } from '../types/book.types'

export async function getBookInstances(): Promise<BookInstance[]> {
  const response = await api.get<BookInstance[]>('/api/book-instances')
  return response.data
}

export async function searchBookInstancesByTitle(title: string): Promise<BookInstance[]> {
  const response = await api.get<BookInstance[]>('/api/book-instances/search', { params: { title } })
  return response.data
}

export async function getBookInstancesByCategory(category: number): Promise<BookInstance[]> {
  const response = await api.get<BookInstance[]>('/api/book-instances/filter', { params: { category } })
  return response.data
}

export async function getBookInstanceById(id: number): Promise<BookInstance> {
  const response = await api.get<BookInstance>(`/api/book-instances/${id}`)
  return response.data
}
