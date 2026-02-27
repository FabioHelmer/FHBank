package br.com.fhbank.services;

import br.com.fhbank.dtos.usuario.UsuarioCreateDTO;
import br.com.fhbank.dtos.usuario.UsuarioResponseDTO;
import br.com.fhbank.dtos.usuario.UsuarioUpdateDTO;
import br.com.fhbank.enums.TipoUsuario;
import br.com.fhbank.exception.BusinessException;
import br.com.fhbank.exception.EntityNotFoundException;
import br.com.fhbank.models.Usuario;
import br.com.fhbank.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /* ========= CREATE ========= */

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO dto) {

        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .cpf(dto.cpf())
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .tipo(dto.tipo())
                .build();

        return toResponse(repository.save(usuario));
    }


    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioUpdateDTO dto) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(dto.email())
                && repository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());

        return toResponse(usuario);
    }


    @Transactional
    public void deletar(UUID id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if (usuario.getConta() != null) {
            throw new BusinessException("Usuário possui conta vinculada");
        }
        repository.delete(usuario);
    }


    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarPorTipo(TipoUsuario tipo) {
        return repository.findAllByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );
    }
}