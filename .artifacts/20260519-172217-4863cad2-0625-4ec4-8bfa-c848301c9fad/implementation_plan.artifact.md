# Implementation Plan - Dynamic Exercise and Activity History

This plan outlines the steps to dynamically populate the "Historial de Actividades" and "Historial de Ejercicios" panels in `ResumenActivity` with data from the API.

## User Review Required

> [!NOTE]
> I will use `AvanceData` for "Historial de Ejercicios" (showing points and dates) and `ActividadData` for "Historial de Actividades" (showing programmed vs. completed repetitions).

## Proposed Changes

### Resources

#### [NEW] [item_historial.xml](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/res/layout/item_historial.xml)
- Create a generic item layout for the history entries, styled to match the app's aesthetic.

#### [desplegable_actividades.xml](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/res/layout/desplegable_actividades.xml)
- Add a `LinearLayout` with id `container_actividades` inside the `ScrollView` to hold the dynamic items.

#### [desplegable_ejercicios.xml](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/res/layout/desplegable_ejercicios.xml)
- Add a `LinearLayout` with id `container_ejercicios` inside the `ScrollView` to hold the dynamic items.

---

### Activities

#### [ResumenActivity.kt](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/java/com/example/tervi/ResumenActivity.kt)
- Create a helper function `mostrarHistorial(layoutId: Int, list: List<Any>?)` to handle the inflation and population of the bottom sheet.
- Store the latest API response in a class variable to avoid re-fetching data when clicking buttons.
- Update click listeners to pass the data to the helper function.

```kotlin
    private fun setupDynamicPanel(layoutId: Int, isActivities: Boolean) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(layoutId, null)
        val container = view.findViewById<LinearLayout>(if (isActivities) R.id.container_actividades else R.id.container_ejercicios)

        // Populate container based on data
        // ...

        dialog.setContentView(view)
        dialog.show()
    }
```

## Verification Plan

### Automated Tests
- Build the project: `gradlew assembleDebug`

### Manual Verification
1. Open the app and go to "Resumen".
2. Click on "Actividades". Verify that a list of activities appears if data is available.
3. Click on "Ejercicios". Verify that a list of exercise advances appears.
4. Verify that the ScrollView allows scrolling if there are many records.
