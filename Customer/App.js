import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import React from "react";

import CoursesScreen from './screens/CoursesScreen';
import CourseDetailScreen from './screens/CourseDetailScreen';
import ClassesScreen from "./screens/ClassesScreen";
import ClassDetailScreen from "./screens/ClassDetailScreen";

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Courses" screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Courses" component={CoursesScreen} />
        <Stack.Screen name="CourseDetail" component={CourseDetailScreen} />
        <Stack.Screen name="Classes" component={ClassesScreen} />
        <Stack.Screen name="ClassDetail" component={ClassDetailScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
