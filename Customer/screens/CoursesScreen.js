import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Button,
  FlatList,
  TouchableOpacity,
  StyleSheet,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import { ref, onValue } from "firebase/database";
import { database } from "../firebaseConfig";

export default function CoursesScreen() {
  const navigation = useNavigation();
  const [courses, setCourses] = useState([]);

  console.log(courses);

  useEffect(() => {
    const coursesRef = ref(database, "courses/");
    const showData = onValue(coursesRef, (snapshot) => {
      const coursesData = snapshot.val();
      const coursesList = coursesData
        ? Object.keys(coursesData).map((key) => ({
            id: key,
            ...coursesData[key],
          }))
        : [];
      setCourses(coursesList);
    });

    return () => showData();
  }, []);

  if (courses.length == 0) {
    return (
      <View style={styles.container}>
        <Text style={styles.loadingText}>No courses available</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <FlatList
        data={courses}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.courseItem}>
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("Classes", { courseId: item.id })
              }
            >
              <Text style={styles.courseName}>Course {item.id}</Text>
            </TouchableOpacity>
            <Button
              title="More"
              onPress={() =>
                navigation.navigate("CourseDetail", { courseId: item.id })
              }
              color="#007bff"
            />
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    paddingTop: 55,
    backgroundColor: "#f8f9fa",
  },
  loadingText: {
    fontSize: 18,
    textAlign: "center",
    marginTop: 20,
    paddingTop: 55,
  },
  courseItem: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: "#ffffff",
    borderRadius: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 3,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  courseName: {
    fontSize: 18,
    fontWeight: "600",
  },
});
