# 🎋 Vietnamese Lunar Calendar & Event Manager

**Vietnamese Lunar Calendar & Event Manager** (*Lịch Âm & Sự Kiện*) is a native Android application designed to look up accurate astronomical Vietnamese Lunar-Solar dates (GMT+7) and manage recurring lunar events. Built with **Jetpack Compose (Material 3)**, it embraces a royal Vietnamese aesthetic (*"Imperial Jade & Gold"*).

---

## 🌟 Key Features

- **Today Dashboard (Home)**:
  - Look up Solar date, Lunar date, Can Chi (Stem-Branch calendar) accompanied by **12 Zodiac animal badges**.
  - Check **Auspicious Hours** (*Giờ Hoàng Đạo*) and **Inauspicious Hours** (*Giờ Hắc Đạo*).
  - Daily curated Vietnamese proverbs and folk sayings.
- **Full Month Calendar View**:
  - Complete 6x7 monthly grid with prominent Solar dates and Lunar dates underneath.
  - Interactive date inspection showing Can Chi, Solar Terms (*Tiết Khí*), and auspicious indicators.
- **Event Management**:
  - Add recurring Lunar events (birthdays, festivals, family events), automatically converted and displayed as corresponding Solar dates for the current year.
  - Smart event countdown badge (*"In X days"* or *"Today!"*).
- **Solar ⇄ Lunar Date Converter**:
  - Bi-directional conversion using a smooth **Wheel Picker** with automatic month length validation.
- **3 Desktop Home Screen Widgets 4x4 (Jetpack Glance)**:
  - `Month Calendar (Light)`: Imperial Jade & Gold theme.
  - `Month Calendar (Dark)`: Nocturnal Obsidian & Gold theme.
  - `Month Calendar (Transparent)`: Translucent glass effect matching any wallpaper.
- **Dark Theme Support**:
  - Instant theme switching between Light (*Imperial Jade*) and Dark (*Nocturnal Obsidian*) modes.

---

## 🛠 Tech Stack & Architecture

- **Language**: Kotlin + Coroutines & StateFlow.
- **UI Framework**: Jetpack Compose (Material 3) + Custom Canvas Graphics (`drawWithContent`).
- **Architecture**: MVVM + Unidirectional Data Flow (UDF).
- **Persistence**: Room Database (Offline first).
- **Home Screen Widgets**: Jetpack Glance (`1.1.0`).
- **Core Engine**: Hồ Ngọc Đức Astronomical Lunar Algorithm (UTC+7 / GMT+7).
