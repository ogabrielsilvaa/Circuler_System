import { colors } from "./src/styles/colors";
import { fontFamily } from "./src/styles/fontFamily";

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{ts,tsx}',
    './src/**/*.{ts,tsx}',
  ],
  presets: [require('nativewind/preset')],
  theme: {
    extend: {
      colors: colors,
      fontFamily: {
        regular: fontFamily.regular,
        medium: fontFamily.medium,
        bold: fontFamily.bold,
      }
    },
  },
  plugins: [],
}
