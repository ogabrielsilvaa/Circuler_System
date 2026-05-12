import { View, Text, TextInput, TextInputProps } from 'react-native';

type InputProps = TextInputProps & {
  label: string;
  error?: string;
  inputClassName?: string;
  labelClassName?: string;
};

export function Input({ label, error, inputClassName, labelClassName, ...props }: InputProps) {
  return (
    <View className="w-full mb-4">
      <Text className={`font-medium text-sm mb-1 ${labelClassName ?? 'text-white'}`}>{label}</Text>
      <TextInput
        className={`rounded-xl px-4 py-3 text-base ${error ? 'border border-red-400' : 'border border-transparent'} ${inputClassName ?? ''}`}
        {...props}
      />
      {error ? (
        <Text className="text-red-400 text-xs mt-1">{error}</Text>
      ) : null}
    </View>
  );
}
