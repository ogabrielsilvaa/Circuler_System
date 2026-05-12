import { View, Text, ScrollView, KeyboardAvoidingView, Platform } from 'react-native';
import { useRouter, type Href } from 'expo-router';
import { useState, useEffect } from 'react';
import { useRegister } from '../../../hooks/useAuth';
import { RegisterFormValues } from '../../../types/auth.types';
import { Button } from '../../../components/Button';
import { Input } from '../../../components/Input';
import { LoginHeader } from '../-components/LoginHeader';

export default function Register() {
  const router = useRouter();
  const { submitRegister, isLoading, isSuccess, fieldErrors } = useRegister();

  const [form, setForm] = useState<RegisterFormValues>({ name: '', email: '', password: '' });

  useEffect(() => {
    if (isSuccess) {
      router.replace('/(auth)/login' as unknown as Href);
    }
  }, [isSuccess]);

  function handleChange(field: keyof RegisterFormValues, value: string): void {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  function handleRegister(): void {
    submitRegister(form);
  }

  function handleLogin(): void {
    router.back();
  }

  return (
    <KeyboardAvoidingView
      className="flex-1 bg-emerald-800"
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView
        contentContainerClassName="flex-grow items-center justify-center px-6 py-12"
        keyboardShouldPersistTaps="handled"
      >
        <LoginHeader />

        <View className="w-full max-w-sm">
          <Input
            label="Nome"
            value={form.name}
            onChangeText={(v) => handleChange('name', v)}
            error={fieldErrors.name}
            inputClassName="bg-emerald-700 text-white"
            placeholderTextColor="#6ee7b7"
            autoCapitalize="words"
            autoCorrect={false}
            editable={!isLoading}
            placeholder="Seu Nome Completo"
          />

          <Input
            label="E-mail"
            value={form.email}
            onChangeText={(v) => handleChange('email', v)}
            error={fieldErrors.email}
            inputClassName="bg-emerald-700 text-white"
            placeholderTextColor="#6ee7b7"
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
            editable={!isLoading}
            placeholder="seu@email.com"
          />

          <Input
            label="Senha"
            value={form.password}
            onChangeText={(v) => handleChange('password', v)}
            error={fieldErrors.password}
            inputClassName="bg-emerald-700 text-white"
            placeholderTextColor="#6ee7b7"
            secureTextEntry
            editable={!isLoading}
            placeholder="••••••••"
          />

          {fieldErrors.general ? (
            <Text className="text-red-400 text-sm text-center mb-4">
              {fieldErrors.general}
            </Text>
          ) : null}

          <Button
            label={isLoading ? 'Cadastrando...' : 'Cadastrar'}
            variant="primary"
            className="mb-3"
            onPress={handleRegister}
            disabled={isLoading}
          />

          <Button
            label="Entrar"
            variant="secondary"
            onPress={handleLogin}
            disabled={isLoading}
          />
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}
