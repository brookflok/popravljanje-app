import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IJavnoPitanje } from 'app/shared/model/javno-pitanje.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './javno-pitanje.reducer';

export interface IJavnoPitanjeDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JavnoPitanjeDeleteDialog = (props: IJavnoPitanjeDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/javno-pitanje');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.javnoPitanjeEntity.id);
  };

  const { javnoPitanjeEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="popravljanjeApp.javnoPitanje.delete.question">
        <Translate contentKey="popravljanjeApp.javnoPitanje.delete.question" interpolate={{ id: javnoPitanjeEntity.id }}>
          Are you sure you want to delete this JavnoPitanje?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-javnoPitanje" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ javnoPitanje }: IRootState) => ({
  javnoPitanjeEntity: javnoPitanje.entity,
  updateSuccess: javnoPitanje.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JavnoPitanjeDeleteDialog);
