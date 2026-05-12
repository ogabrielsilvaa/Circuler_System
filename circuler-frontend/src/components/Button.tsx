import { TouchableOpacity, Text, TouchableOpacityProps } from 'react-native';

type ButtonVariant = 'primary' | 'secondary';

type ButtonProps = TouchableOpacityProps & {
  label: string;
  variant?: ButtonVariant;
};

export function Button({ label, variant = 'primary', className, ...props }: ButtonProps) {
  const containerClass =
    variant === 'primary'
      ? `bg-emerald-600 rounded-xl py-4 items-center w-full ${className ?? ''}`
      : `bg-white rounded-xl py-4 items-center w-full ${className ?? ''}`;

  const textClass =
    variant === 'primary'
      ? 'text-white font-bold text-base'
      : 'text-emerald-800 font-bold text-base';

  return (
    <TouchableOpacity className={containerClass} activeOpacity={0.8} {...props}>
      <Text className={textClass}>{label}</Text>
    </TouchableOpacity>
  );
}
