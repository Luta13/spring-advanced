package org.example.expert.domain.todo;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest{

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private TodoService todoService;

    @Test
    public void todo_저장을_성공한다(){
        AuthUser authUser = new AuthUser(1L, "test123@gmail.com", UserRole.USER);
        TodoSaveRequest saveRequest = new TodoSaveRequest("dd","dd");
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo(saveRequest.getTitle(),saveRequest.getContents(),weatherClient.getTodayWeather(),user);
        given(todoRepository.save(todo)).willReturn(todo);
        Todo savedTodo = todoRepository.save(todo);


        assertEquals(todo.getId(), savedTodo.getId());
        assertEquals(todo.getTitle(), savedTodo.getTitle());
        assertEquals(todo.getContents(), savedTodo.getContents());
        assertEquals(todo.getUser(), savedTodo.getUser());
    }

    @Test
    public void todo_단건_조회_실패() {
        // given
        long todoId = 1L;


        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            todoService.getTodo(todoId);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }
    @Test
    public void todo_단건_조회를_성공한다() {
        // given
        long todoId = 1L;

        User user = new User("email", "password", UserRole.USER);
        Todo todo = new Todo("title", "contents","Sunny", user);
        User todoUser = todo.getUser();

        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(todo));

        // when
        TodoResponse result = todoService.getTodo(todoId);

        // then
        Assertions.assertNotNull(result);
        assertEquals(todo.getId(), result.getId());
        assertEquals(todo.getTitle(), result.getTitle());
        assertEquals(todo.getContents(), result.getContents());
        assertEquals(todo.getWeather(), result.getWeather());
        assertEquals(todoUser.getId(), result.getUser().getId());
        assertEquals(todoUser.getEmail(), result.getUser().getEmail());
    }





}
